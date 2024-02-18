package com.github.thedeathlycow.scorchful.util;

import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class SMth {

    public static void lerpMutable(float delta, Vector3f start, Vector3f end, Vector3f output) {
        output.x = MathHelper.lerp(delta, start.x, end.x);
        output.y = MathHelper.lerp(delta, start.y, end.y);
        output.z = MathHelper.lerp(delta, start.z, end.z);
    }

    private SMth() {

    }
}
