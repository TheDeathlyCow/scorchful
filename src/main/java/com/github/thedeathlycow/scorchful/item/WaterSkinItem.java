package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.api.CollectWaterCallback;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterSkinItem extends DrinkItem {

    public static final Style TOOLTIP_STYLE = Style.EMPTY
            .withColor(Formatting.AQUA);

    public static final Style PARCHING_STYLE = Style.EMPTY
            .withColor(Formatting.RED);

    public static final int MAX_DRINKS = 16;

    public static final Text FULL_ITEM_NAME = Text.translatable("item.scorchful.water_skin.filled");
    public static final Text PARTIALLY_FULL_ITEM_NAME = Text.translatable("item.scorchful.water_skin.partially_filled");
    public static final Text EMPTY_ITEM_NAME = Text.translatable("item.scorchful.water_skin.empty");

    public WaterSkinItem(Settings settings) {
        super(settings);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(this, this::onCauldronInteract);
    }

    public static ConsumableComponent consumable() {
        return ConsumableComponent.builder()
                .sound(SoundEvents.ENTITY_GENERIC_DRINK)
                .useAction(UseAction.DRINK)
                .consumeSeconds(DRINK_TIME_TICKS / 20f)
                .consumeParticles(false)
                .build();
    }

    @Override
    protected ItemStack getPostConsumeStack(ItemStack stack, World world, ServerPlayerEntity serverPlayer) {
        if (!serverPlayer.isCreative()) {
            addDrinks(stack, -1);
        }

        return stack;
    }

    @Override
    public ItemStack getDefaultStack() {
        var itemStack = super.getDefaultStack();
        itemStack.set(SDataComponentTypes.NUM_DRINKS, 0);
        return itemStack;
    }

    public static int getNumDrinks(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.NUM_DRINKS, 0);
    }

    public static float getFill(ItemStack stack) {
        return (float) getNumDrinks(stack) / MAX_DRINKS;
    }

    public static boolean hasDrink(ItemStack stack) {
        return getNumDrinks(stack) > 0;
    }

    public static void addDrinks(ItemStack stack, int value) {
        stack.apply(
                SDataComponentTypes.NUM_DRINKS,
                0,
                currentDrinks -> MathHelper.clamp(currentDrinks + value, 0, MAX_DRINKS)
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType tooltipType) {
        int numDrinks = getNumDrinks(stack);

        if (numDrinks > 0) {
            super.appendTooltip(stack, context, tooltip, tooltipType);
        }

        MutableText text = numDrinks > 0
                ? Text.translatable("item.scorchful.water_skin.tooltip.count", numDrinks, MAX_DRINKS)
                : Text.translatable("item.scorchful.water_skin.tooltip.empty");
        text.setStyle(TOOLTIP_STYLE);

        tooltip.add(text);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        ActionResult refillResult = this.tryRefill(world, user, stack);
        if (refillResult != null) {
            return refillResult;
        }

        if (hasDrink(stack)) {
            return super.use(world, user, hand);
        }

        return ActionResult.PASS;
    }

    @Override
    public Text getName(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);

        return switch (numDrinks) {
            case 0 -> EMPTY_ITEM_NAME;
            case MAX_DRINKS -> FULL_ITEM_NAME;
            default -> PARTIALLY_FULL_ITEM_NAME;
        };
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return hasDrink(stack) ? DrinkItem.DRINK_TIME_TICKS : 0;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);
        return numDrinks < MAX_DRINKS;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);
        if (numDrinks == 0) {
            return 0;
        }

        return Math.round(((float) numDrinks / MAX_DRINKS) * 13.0f);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float fill = Math.max(0.0f, getFill(stack));

        float saturationValue = MathHelper.clampedMap(fill, 0f, 1f, 0.5f, 1.0f);

        return MathHelper.hsvToRgb(210f / 360f, saturationValue, saturationValue);
    }

    protected void fill(ItemStack stack, PlayerEntity player, World world, BlockPos sourcePos, int amount) {
        world.playSound(
                null,
                player.getBlockPos(),
                SSoundEvents.ITEM_WATER_SKIN_FILL, SoundCategory.PLAYERS,
                1.0f, 1.0f
        );
        world.emitGameEvent(player, GameEvent.FLUID_PICKUP, sourcePos);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        addDrinks(stack, amount);

        CollectWaterCallback.EVENT.invoker().onWaterCollected(player, stack, sourcePos);
    }

    @Nullable
    private ActionResult tryRefill(World world, PlayerEntity user, ItemStack stack) {
        BlockHitResult blockHitResult = Item.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            if (!world.canPlayerModifyAt(user, hitPos) || getNumDrinks(stack) >= MAX_DRINKS) {
                return null;
            }

            if (world.getFluidState(hitPos).isIn(FluidTags.WATER)) {
                if (!world.isClient) {
                    this.fill(stack, user, world, hitPos, 4);
                }
                return ActionResult.SUCCESS;
            }
        }
        return null;
    }

    private ActionResult onCauldronInteract(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack
    ) {

        if (!world.isClient) {
            this.fill(stack, player, world, pos, 1);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.SUCCESS;
    }

    public ActionResult onWarpedLilyInteract(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            ItemStack stack
    ) {
        if (getNumDrinks(stack) >= MAX_DRINKS) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        }

        if (state.get(NetherLilyBlock.WATER_SATURATION_LEVEL) < 3) {
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            this.fill(stack, player, world, pos, 4);
            player.incrementStat(SStats.USE_WARPED_LILY);
            NetherLilyBlock.setWater(state, world, pos, 0);
        }
        return ActionResult.SUCCESS;
    }

}
