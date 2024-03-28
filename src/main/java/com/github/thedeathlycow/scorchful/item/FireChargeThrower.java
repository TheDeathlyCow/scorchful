package com.github.thedeathlycow.scorchful.item;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class FireChargeThrower implements UseItemCallback {

    private static final int FIRE_CHARGE_COOL_DOWN = 20;

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (player.isSpectator()) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.FIRE_CHARGE)) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }
        if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            return TypedActionResult.fail(stack);
        }

        // spawn fire charge entity
        if (!world.isClient) {
            Vec3d rotation = player.getRotationVector();
            AbstractFireballEntity fireball = new SmallFireballEntity(
                    world,
                    player,
                    rotation.x, rotation.y, rotation.z
            );
            fireball.setPosition(fireball.getX(), player.getBodyY(0.5) + 0.5, fireball.getZ());
            fireball.setItem(stack);
            world.spawnEntity(fireball);
            world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, player.getBlockPos(), 0);
        }

        // decrement stack
        if (!player.isCreative()) {
            stack.decrement(1);
        }
        player.getItemCooldownManager().set(stack.getItem(), FIRE_CHARGE_COOL_DOWN);

        return TypedActionResult.success(stack);
    }

}
