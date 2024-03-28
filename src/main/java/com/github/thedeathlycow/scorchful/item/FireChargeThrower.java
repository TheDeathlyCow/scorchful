package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireChargeThrower implements UseItemCallback {

    private static final int FIRE_CHARGE_COOL_DOWN = 20;

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (player.isSpectator()) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }

        ScorchfulConfig config = Scorchful.getConfig();
        FireballFactory throwingTypes = config.combatConfig.getFireBallThrownType();

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.FIRE_CHARGE) || throwingTypes == FireballFactory.DISABLED) {
            return TypedActionResult.pass(ItemStack.EMPTY);
        }
        if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            return TypedActionResult.fail(stack);
        }

        // spawn fire charge entity
        if (!world.isClient) {
            Vec3d rotation = player.getRotationVector();
            AbstractFireballEntity fireball = throwingTypes.create(world, player, rotation);

            if (fireball == null) {
                return TypedActionResult.pass(ItemStack.EMPTY);
            }

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

    public enum FireballFactory {

        DISABLED {
            @Override
            @Nullable
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return null;
            }
        },
        SMALL {
            @Override
            @NotNull
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return new SmallFireballEntity(
                        world, player,
                        velocity.x, velocity.y, velocity.z
                );
            }
        },
        LARGE {
            @Override
            @NotNull
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return new FireballEntity(
                        world, player,
                        velocity.x, velocity.y, velocity.z, 1
                );
            }
        };

        @Nullable
        public abstract AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity);

    }

}
