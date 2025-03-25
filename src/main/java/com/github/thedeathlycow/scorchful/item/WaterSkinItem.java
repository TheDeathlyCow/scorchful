package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.api.CollectWaterCallback;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.SStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.type.ConsumableComponent;
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
            DrinkContainerComponent.addDrinks(stack, -1);
        }

        return stack;
    }

    @Override
    public ItemStack getDefaultStack() {
        var itemStack = super.getDefaultStack();
        itemStack.set(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT);
        return itemStack;
    }

    public static DrinkContainerComponent getContainer(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT);
    }

    public static boolean hasDrink(ItemStack stack) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT).hasDrink();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType tooltipType) {
        DrinkContainerComponent container = getContainer(stack);

        if (container.numDrinks() > 0) {
            super.appendTooltip(stack, context, tooltip, tooltipType);
        }

        MutableText text = container.hasDrink()
                ? Text.translatable("item.scorchful.water_skin.tooltip.count", container.numDrinks(), container.maxDrinks())
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
        DrinkContainerComponent container = getContainer(stack);

        if (container.isEmpty()) {
            return EMPTY_ITEM_NAME;
        } else if (container.isFull()) {
            return FULL_ITEM_NAME;
        } else {
            return PARTIALLY_FULL_ITEM_NAME;
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return !getContainer(stack).isFull();
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        DrinkContainerComponent container = getContainer(stack);

        if (container.isEmpty()) {
            return 0;
        }

        return Math.round(container.getCurrentFill() * 13.0f);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float fill = Math.max(0.0f, getContainer(stack).getCurrentFill());

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
        DrinkContainerComponent.addDrinks(stack, amount);

        CollectWaterCallback.EVENT.invoker().onWaterCollected(player, stack, sourcePos);
    }

    @Nullable
    private ActionResult tryRefill(World world, PlayerEntity user, ItemStack stack) {
        BlockHitResult blockHitResult = Item.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            DrinkContainerComponent container = stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT);

            if (!world.canPlayerModifyAt(user, hitPos) || container.isFull()) {
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
        if (getContainer(stack).isFull()) {
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
