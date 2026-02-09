package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.event.HeatVisionActivation;
import com.github.thedeathlycow.scorchful.particle.DustGrainParticleEffect;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import org.joml.Vector3fc;

public class ApproachEffects {
    private static final Vector3fc COLOR = ARGB.vector3fFromRGB24(0xD9AA84);

    public static void initialize() {
        HeatVisionActivation.EVENT.register((vision, world, pos, player) -> {
            var particle = new DustGrainParticleEffect(COLOR, 1f);
            world.sendParticles(
                    particle,
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    1000,
                    1, 1, 1,
                    0.01
            );
        });
        HeatVisionActivation.EVENT.register((vision, world, pos, player) -> {
            world.playSound(null, pos, SSoundEvents.DISCOVER_VISION, SoundSource.AMBIENT);
        });
    }

    private ApproachEffects() {

    }


}
