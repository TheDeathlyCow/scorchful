package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.client.config.ScorchfulClientConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.concurrent.ThreadLocalRandom;

public final class SoakedEffects {

    private static final float SLOW_DRIP_MULTIPLIER = 2.0f;

    /**
     * Renders water particles on players that are wet. The chance of a drip spawning is the same
     * as the player's wetness scale.
     * This is done on the client side to avoid sending unnecessary packets and save bandwidth.
     */
    public static void tickDripParticles(Player player, Level world, boolean submergedInWater) {
        if (world.isClientSide()) { // only show particles on client to save bandwidth

            // config to disable
            if (!ScorchfulClientConfig.getDisplaySettings().enableWetDripParticles()) {
                return;
            }

            // spectators should not drip
            if (player.isSpectator()) {
                return;
            }

            // only spawn particles when out of water
            if (submergedInWater) {
                return;
            }

            // Ensure that only players with non-zero wetness have particles
            // (I mostly just don't trust floats lol)
            if (!player.thermoo$isWet()) {
                return;
            }

            ThreadLocalRandom random = ThreadLocalRandom.current();

            // Spawn drip with probability proportional to wetness scale
            if (SLOW_DRIP_MULTIPLIER * random.nextFloat() < player.thermoo$getSoakedScale()) {

                AABB boundingBox = player.getBoundingBox();

                // pick random pos in player bounding box
                double x = boundingBox.min(Direction.Axis.X) + random.nextDouble(boundingBox.getXsize());
                double y = boundingBox.min(Direction.Axis.Y) + random.nextDouble(boundingBox.getYsize());
                double z = boundingBox.min(Direction.Axis.Z) + random.nextDouble(boundingBox.getZsize());

                world.addParticle(
                        ParticleTypes.FALLING_DRIPSTONE_WATER,
                        x, y, z,
                        0, 0, 0
                );
            }
        }
    }

    private SoakedEffects() {

    }
}
