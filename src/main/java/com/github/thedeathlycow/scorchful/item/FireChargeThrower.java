package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireChargeThrower implements UseItemCallback {

    private static final int FIRE_CHARGE_COOL_DOWN = 20;

    @Override
    public InteractionResultHolder<ItemStack> interact(Player player, Level world, InteractionHand hand) {
        if (player.isSpectator()) {
            return InteractionResultHolder.pass(ItemStack.EMPTY);
        }

        ScorchfulConfig config = Scorchful.getConfig();
        FireballFactory throwingTypes = config.combatConfig.getFireBallThrownType();

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.FIRE_CHARGE) || throwingTypes == FireballFactory.DISABLED) {
            return InteractionResultHolder.pass(ItemStack.EMPTY);
        }
        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            return InteractionResultHolder.fail(stack);
        }

        // spawn fire charge entity
        if (!world.isClientSide) {
            Vec3 rotation = player.getLookAngle();
            Fireball fireball = throwingTypes.create(world, player, rotation);

            if (fireball == null) {
                return InteractionResultHolder.pass(ItemStack.EMPTY);
            }

            fireball.setPos(fireball.getX(), player.getY(0.5) + 0.5, fireball.getZ());
            fireball.setItem(stack);
            world.addFreshEntity(fireball);
            world.levelEvent(null, LevelEvent.SOUND_BLAZE_FIREBALL, player.blockPosition(), 0);
        }

        // decrement stack
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        player.getCooldowns().addCooldown(stack.getItem(), FIRE_CHARGE_COOL_DOWN);

        return InteractionResultHolder.success(stack);
    }

    public enum FireballFactory {

        DISABLED {
            @Override
            @Nullable
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return null;
            }
        },
        SMALL {
            @Override
            @NotNull
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return new SmallFireball(
                        world, player,
                        velocity
                );
            }
        },
        LARGE {
            @Override
            @NotNull
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return new LargeFireball(
                        world, player,
                        velocity, 1
                );
            }
        };

        @Nullable
        public abstract Fireball create(Level world, Player player, Vec3 velocity);

    }

}
