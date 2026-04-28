package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.event.HeatVisionActivation;
import com.github.thedeathlycow.scorchful.particle.DustGrainParticleEffect;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ApproachEffects {


    private static final Vector3f COLOR = Vec3.fromRGB24(0xD9AA84).toVector3f();

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
