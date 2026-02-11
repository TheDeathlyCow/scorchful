package com.github.thedeathlycow.scorchful.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.joml.Vector3f;

public class SMth {

    public static void lerpMutable(float delta, Vector3f start, Vector3f end, Vector3f output) {
        output.x = Mth.lerp(delta, start.x, end.x);
        output.y = Mth.lerp(delta, start.y, end.y);
        output.z = Mth.lerp(delta, start.z, end.z);
    }

    public static double nextGaussian(RandomSource random, double mean, double deviation) {
        return mean + random.nextGaussian() * deviation;
    }

    @Contract("_,_,_->new")
    public static Vec3 lerp(float delta, Vec3 start, Vec3 end) {
        return new Vec3(
                Mth.lerp(delta, start.x, end.x),
                Mth.lerp(delta, start.y, end.y),
                Mth.lerp(delta, start.z, end.z)
        );
    }

    private SMth() {

    }
}
