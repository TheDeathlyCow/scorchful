package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
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
    public InteractionResult interact(Player player, Level world, InteractionHand hand) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        }

        FireballFactory throwingTypes = ScorchfulConfig.getItemConfig().getFireBallThrownType();

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.FIRE_CHARGE) || throwingTypes == FireballFactory.DISABLED) {
            return InteractionResult.PASS;
        }
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        // spawn fire charge entity
        if (!world.isClientSide()) {
            Vec3 rotation = player.getLookAngle();
            Fireball fireball = throwingTypes.create(world, player, rotation);

            if (fireball == null) {
                return InteractionResult.PASS;
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
        player.getCooldowns().addCooldown(stack, FIRE_CHARGE_COOL_DOWN);

        return InteractionResult.SUCCESS;
    }

    public enum FireballFactory implements StringRepresentable {
        DISABLED("disabled") {
            @Override
            @Nullable
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return null;
            }
        },
        SMALL("small") {
            @Override
            @NotNull
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return new SmallFireball(
                        world, player,
                        velocity
                );
            }
        },
        LARGE("large") {
            @Override
            @NotNull
            public Fireball create(Level world, Player player, Vec3 velocity) {
                return new LargeFireball(
                        world, player,
                        velocity, 1
                );
            }
        };

        private final String name;

        FireballFactory(String name) {
            this.name = name;
        }

        @Nullable
        public abstract Fireball create(Level world, Player player, Vec3 velocity);


        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
