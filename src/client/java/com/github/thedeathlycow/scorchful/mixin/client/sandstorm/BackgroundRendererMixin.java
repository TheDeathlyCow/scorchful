package com.github.thedeathlycow.scorchful.mixin.client.sandstorm;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin { //NOSONAR
    @Inject(
            method = "getFogColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/world/ClientWorld$Properties;getHorizonShadingRatio()F",
                    shift = At.Shift.AFTER
            )
    )
    private static void setFogColorForSandstorm(
            Camera camera,
            float tickDelta,
            ClientWorld world,
            int clampedViewDistance,
            float skyDarkness,
            CallbackInfoReturnable<Vector4f> cir,
            @Local(ordinal = 2) LocalFloatRef red,
            @Local(ordinal = 3) LocalFloatRef green,
            @Local(ordinal = 4) LocalFloatRef blue
    ) {
        Vec3d color = SandstormEffects.getFogColor(
                world, camera,
                red.get(), green.get(), blue.get(),
                tickDelta
        );

        if (color != null) {
            red.set((float) color.x);
            green.set((float) color.y);
            blue.set((float) color.z);
        }
    }

    @Inject(
            method = "applyFog",
            at = @At("TAIL")
    )
    private static void setFogDistanceForSandstorm(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            Vector4f color,
            float viewDistance,
            boolean thickenFog,
            float tickDelta,
            CallbackInfoReturnable<Fog> cir,
            @Local CameraSubmersionType cameraSubmersionType,
            @Local BackgroundRenderer.FogData fogData
    ) {
        SandstormEffects.updateFogDistance(camera, viewDistance, cameraSubmersionType, fogData);
    }

}
