package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class WaterSkinItem extends Item {
    public WaterSkinItem(Settings settings) {
        super(settings);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
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

                this.fill(itemStack, user);

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

        stack.setDamage(stack.getDamage() + 1);

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

    private void fill(ItemStack stack, PlayerEntity player) {
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        int damage = stack.getDamage();
        stack.setDamage(damage - 1);
    }

}
