package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public final class SoakedEffects {

    private static final float SLOW_DRIP_MULTIPLIER = 2.0f;

    /**
     * Renders water particles on players that are wet. The chance of a drip spawning is the same
     * as the player's wetness scale.
     * This is done on the client side to avoid sending unnecessary packets and save bandwidth.
     */
    public static void tickDripParticles(PlayerEntity player, World world, boolean submergedInWater) {
        if (world.isClient) { // only show particles on client to save bandwidth

            // config to disable
            if (!Scorchful.getConfig().clientConfig.enableWetDripParticles()) {
                return;
            }

            // spectators should not drip
            if (player.isSpectator()) {
                return;
            }

            // only spawn particles when out of water
            if (submergedInWater || player.isWet()) {
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

                Box boundingBox = player.getBoundingBox();

                // pick random pos in player bounding box
                double x = boundingBox.getMin(Direction.Axis.X) + random.nextDouble(boundingBox.getXLength());
                double y = boundingBox.getMin(Direction.Axis.Y) + random.nextDouble(boundingBox.getYLength());
                double z = boundingBox.getMin(Direction.Axis.Z) + random.nextDouble(boundingBox.getZLength());

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
