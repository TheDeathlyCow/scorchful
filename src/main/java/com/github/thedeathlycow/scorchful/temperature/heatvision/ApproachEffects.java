package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.event.HeatVisionActivation;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ApproachEffects {


    private static final Vector3f COLOR = Vec3d.unpackRgb(0xD9AA84).toVector3f();

    public static void initialize() {
        HeatVisionActivation.EVENT.register((vision, world, pos, player) -> {
            var particle = new DustParticleEffect(COLOR, 1);
            world.spawnParticles(
                    particle,
                    pos.getX(), pos.getY(), pos.getZ(),
                    200,
                    1, 1, 1,
                    1
            );
        });
    }

    private ApproachEffects() {

    }


}
