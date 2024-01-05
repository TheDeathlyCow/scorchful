package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterSkinItem extends Item {

    public static final int MAX_DRINKS = 10;
    private static final String DRINK_NBT_KEY = "drinks";

    private static final String COUNT_NBT_KEY = "count";


    private static final Style TOOLTIP_STYLE = Style.EMPTY
            .withColor(TextColor.parse("aqua"));

    public WaterSkinItem(Settings settings) {
        super(settings);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(this, this::onCauldronInteract);
    }

    public static int getNumDrinks(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(DRINK_NBT_KEY);
        if (nbt == null || !nbt.contains(COUNT_NBT_KEY, NbtElement.INT_TYPE)) {
            return 0;
        }
        return nbt.getInt(COUNT_NBT_KEY);
    }

    public static float getFill(ItemStack stack) {
        return (float) getNumDrinks(stack) / MAX_DRINKS;
    }

    public static boolean hasDrink(ItemStack stack) {
        return getNumDrinks(stack) > 0;
    }

    public void addDrinks(ItemStack stack, int value) {
        NbtCompound nbt = stack.getSubNbt(DRINK_NBT_KEY);

        if (!stack.isOf(this)) {
            return;
        }

        if (nbt == null) {
            nbt = new NbtCompound();
            stack.setSubNbt(DRINK_NBT_KEY, nbt);
        }

        if (!nbt.contains(COUNT_NBT_KEY, NbtElement.INT_TYPE)) {
            nbt.putInt(COUNT_NBT_KEY, 0);
        }

        int currentDrinks = nbt.getInt(COUNT_NBT_KEY);
        nbt.putInt(COUNT_NBT_KEY, MathHelper.clamp(currentDrinks + value, 0, MAX_DRINKS));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);

        return switch (numDrinks) {
            case 0 -> "item.scorchful.water_skin.empty";
            case MAX_DRINKS -> "item.scorchful.water_skin.filled";
            default -> "item.scorchful.water_skin.partially_filled";
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int numDrinks = getNumDrinks(stack);

        MutableText text = numDrinks > 0
                ? Text.translatable("item.scorchful.water_skin.tooltip.count", numDrinks, MAX_DRINKS)
                : Text.translatable("item.scorchful.water_skin.tooltip.empty");

        text.setStyle(TOOLTIP_STYLE);
        tooltip.add(text);

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isNbtSynced() {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        TypedActionResult<ItemStack> refillResult = this.tryRefill(world, user, stack);
        if (refillResult != null) {
            return refillResult;
        }

        if (hasDrink(stack)) {
            return ItemUsage.consumeHeldItem(world, user, hand);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!user.isPlayer() || !hasDrink(stack)) {
            return stack;
        }

        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        ScorchfulComponents.PLAYER.get(user).addWater(Scorchful.getConfig().thirstConfig.getWaterFromDrinking());
        this.addDrinks(stack, -1);

        return super.finishUsing(stack, world, user);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return hasDrink(stack) ? 32 : 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return hasDrink(stack) ? UseAction.DRINK : UseAction.NONE;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int numDrinks = getNumDrinks(stack);
        return numDrinks > 0 && numDrinks < MAX_DRINKS;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(((float) getNumDrinks(stack) / MAX_DRINKS) * 13.0f);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float fill = Math.max(0.0f, getFill(stack));

        float saturationValue = MathHelper.clampedMap(fill, 0f, 1f, 0.5f, 1.0f);

        return MathHelper.hsvToRgb(210f / 360f, saturationValue, saturationValue);
    }

    protected void fill(ItemStack stack, PlayerEntity player, World world, BlockPos sourcePos, int amount) {
        player.playSound(SSoundEvents.ITEM_WATER_SKIN_FILL, 1.0f, 1.0f);
        world.emitGameEvent(null, GameEvent.FLUID_PICKUP, sourcePos);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        this.addDrinks(stack, amount);
    }

    @Nullable
    private TypedActionResult<ItemStack> tryRefill(World world, PlayerEntity user, ItemStack stack) {
        BlockHitResult blockHitResult = Item.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            if (!world.canPlayerModifyAt(user, hitPos) || getNumDrinks(stack) >= MAX_DRINKS) {
                return null;
            }

            if (world.getFluidState(hitPos).isIn(FluidTags.WATER)) {
                this.fill(stack, user, world, hitPos, 1);
                return TypedActionResult.success(
                        stack,
                        world.isClient()
                );
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
        if (getNumDrinks(stack) >= MAX_DRINKS) {
            return ActionResult.PASS;
        }

        this.fill(stack, player, world, pos, 1);
        if (!world.isClient) {
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
    }


}
