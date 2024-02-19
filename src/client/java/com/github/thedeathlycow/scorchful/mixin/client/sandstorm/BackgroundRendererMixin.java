package com.github.thedeathlycow.scorchful.mixin.client.sandstorm;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin { //NOSONAR

    @Shadow
    private static float red;

    @Shadow
    private static float green;

    @Shadow
    private static float blue;

    @Inject(
            method = "render",
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
            int viewDistance,
            float skyDarkness,
            CallbackInfo ci
    ) {
        SandstormEffects.getFogColor(
                world, camera.getBlockPos(),
                red, green, blue,
                tickDelta
        ).ifPresent(color -> {
            red = (float) color.x;
            green = (float) color.y;
            blue = (float) color.z;
        });
    }

    @Inject(
            method = "applyFog",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void setFogDistanceForSandstorm(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            float viewDistance,
            boolean thickFog,
            float tickDelta,
            CallbackInfo ci,
            CameraSubmersionType cameraSubmersionType,
            Entity entity,
            BackgroundRenderer.FogData fogData
    ) {
        SandstormEffects.updateFogDistance(camera, viewDistance, cameraSubmersionType, fogData);
    }

}
