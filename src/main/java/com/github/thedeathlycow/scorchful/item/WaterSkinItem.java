package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.PlayerComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.registry.SItemGroups;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

    public static float getFill(ItemStack stack) {
        return ((float) getNumDrinks(stack)) / MAX_DRINKS;
    }

    public static int getNumDrinks(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(DRINK_NBT_KEY);
        if (nbt == null || !nbt.contains(COUNT_NBT_KEY, NbtElement.INT_TYPE)) {
            return 0;
        }
        return nbt.getInt(COUNT_NBT_KEY);
    }

    public boolean addDrinks(ItemStack stack, int value) {
        NbtCompound nbt = stack.getSubNbt(DRINK_NBT_KEY);

        if (!stack.isOf(this)) {
            return false;
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

        return true;
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

    public static boolean isUsable(ItemStack stack) {
        return getNumDrinks(stack) > 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        if (isUsable(itemStack)) {
            ItemUsage.consumeHeldItem(world, user, hand);
        }

        BlockHitResult blockHitResult = Item.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!world.canPlayerModifyAt(user, blockPos)) {
                return TypedActionResult.pass(itemStack);
            }
            if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                world.playSound(
                        user,
                        user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL,
                        1.0f, 1.0f
                );
                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);

                this.fill(itemStack, user, MAX_DRINKS);

                return TypedActionResult.success(
                        itemStack,
                        world.isClient()
                );
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!user.isPlayer() || !isUsable(stack)) {
            return stack;
        }

        ScorchfulComponents.PLAYER.get(user).addWater(Scorchful.getConfig().thirstConfig.getWaterFromDrinking());
        addDrinks(stack, -1);

        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return isUsable(stack) ? 32 : 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return isUsable(stack) ? UseAction.DRINK : UseAction.NONE;
    }

    private void fill(ItemStack stack, PlayerEntity player, int amount) {
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        addDrinks(stack, amount);
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

        if (!world.isClient) {
            this.fill(stack, player, 1);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            // TODO: change this to a new sound
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return ActionResult.success(world.isClient);
    }


}
