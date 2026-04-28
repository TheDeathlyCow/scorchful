package com.github.thedeathlycow.scorchful.mixin.client.sandstorm;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin { //NOSONAR

    @Shadow
    private static float fogRed;

    @Shadow
    private static float fogGreen;

    @Shadow
    private static float fogBlue;

    @Inject(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()F",
                    shift = At.Shift.AFTER
            )
    )
    private static void setFogColorForSandstorm(
            Camera camera,
            float tickDelta,
            ClientLevel world,
            int viewDistance,
            float skyDarkness,
            CallbackInfo ci
    ) {
        SandstormEffects.getFogColor(
                world, camera,
                fogRed, fogGreen, fogBlue,
                tickDelta
        ).ifPresent(color -> {
            fogRed = (float) color.x;
            fogGreen = (float) color.y;
            fogBlue = (float) color.z;
        });
    }

    @Inject(
            method = "setupFog",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void setFogDistanceForSandstorm(
            Camera camera,
            FogRenderer.FogMode fogType,
            float viewDistance,
            boolean thickFog,
            float tickDelta,
            CallbackInfo ci,
            FogType cameraSubmersionType,
            Entity entity,
            FogRenderer.FogData fogData
    ) {
        SandstormEffects.updateFogDistance(camera, viewDistance, cameraSubmersionType, fogData);
    }

}
