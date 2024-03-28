package com.github.thedeathlycow.scorchful.item;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireChargeThrower implements UseItemCallback {

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (player.isSpectator() || world.isClient) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.FIRE_CHARGE)) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        // spawn fire charge entity
        var fireball = EntityType.SMALL_FIREBALL.create(world);

        if (fireball == null) {
            return TypedActionResult.fail(stack);
        }

        fireball.setPosition(player.getX(), player.getBodyY(0.5) + 0.5, player.getZ());

        if (!world.spawnEntity(fireball)) {
            return TypedActionResult.fail(stack);
        }

        // decrement stack
        if (!player.isCreative()) {
            stack.decrement(1);
        }
        player.getItemCooldownManager().set(stack.getItem(), 20);

        return TypedActionResult.success(stack);
    }

}
