package com.github.thedeathlycow.scorchful.util;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityLookUtil {
    public static void lookTowards(
            Entity entity,
            EntityAnchorArgumentType.EntityAnchor anchorPoint,
            Vec3d target,
            float delta
    ) {
        Vec3d anchoredPosition = anchorPoint.positionAt(entity);
        double dx = target.x - anchoredPosition.x;
        double dy = target.y - anchoredPosition.y;
        double dz = target.z - anchoredPosition.z;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        float targetPitch = (float) -MathHelper.atan2(dy, horizontalDistance);
        float targetYaw = (float) MathHelper.atan2(dz, dx);

        targetPitch = MathHelper.wrapDegrees(targetPitch * 180.0f / MathHelper.PI);
        targetYaw = MathHelper.wrapDegrees(targetYaw * 180.0f / MathHelper.PI - 90.0f);

        targetPitch = MathHelper.lerp(delta, entity.getPitch(), targetPitch);
        targetYaw = MathHelper.lerp(delta, entity.getYaw(), targetYaw);

        entity.setPitch(targetPitch);
        entity.setYaw(targetYaw);
        entity.setHeadYaw(entity.getYaw());

        entity.prevPitch = entity.getPitch();
        entity.prevYaw = entity.getYaw();
    }

    private EntityLookUtil() {

    }
}
