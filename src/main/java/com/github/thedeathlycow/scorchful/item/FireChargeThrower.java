package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireChargeThrower implements UseItemCallback {

    private static final int FIRE_CHARGE_COOL_DOWN = 20;

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand) {
        if (player.isSpectator()) {
            return ActionResult.PASS;
        }

        FireballFactory throwingTypes = ScorchfulConfig.getItemConfig().getFireBallThrownType();

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.FIRE_CHARGE) || throwingTypes == FireballFactory.DISABLED) {
            return ActionResult.PASS;
        }
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.FAIL;
        }

        // spawn fire charge entity
        if (!world.isClient()) {
            Vec3d rotation = player.getRotationVector();
            AbstractFireballEntity fireball = throwingTypes.create(world, player, rotation);

            if (fireball == null) {
                return ActionResult.PASS;
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
        player.getItemCooldownManager().set(stack, FIRE_CHARGE_COOL_DOWN);

        return ActionResult.SUCCESS;
    }

    public enum FireballFactory implements StringIdentifiable {
        DISABLED("disabled") {
            @Override
            @Nullable
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return null;
            }
        },
        SMALL("small") {
            @Override
            @NotNull
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return new SmallFireballEntity(
                        world, player,
                        velocity
                );
            }
        },
        LARGE("large") {
            @Override
            @NotNull
            public AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity) {
                return new FireballEntity(
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
        public abstract AbstractFireballEntity create(World world, PlayerEntity player, Vec3d velocity);


        @Override
        public String asString() {
            return this.name;
        }
    }
}
