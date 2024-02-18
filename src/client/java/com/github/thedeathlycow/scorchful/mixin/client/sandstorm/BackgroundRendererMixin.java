package com.github.thedeathlycow.scorchful.mixin.client.sandstorm;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin { //NOSONAR

    @Shadow private static float red;

    @Shadow private static float green;

    @Shadow private static float blue;

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
        BlockPos pos = camera.getBlockPos();
        if (Sandstorms.isSandStorming(world, pos)) {
            float gradient = world.getRainGradient(1f);
            red = MathHelper.lerp(gradient, red, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.x);
            green = MathHelper.lerp(gradient, green, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.y);
            blue = MathHelper.lerp(gradient, blue, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.z);
        }
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
    private static void setFogDistanceForSandstorm( //NOSONAR
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
        if (cameraSubmersionType == CameraSubmersionType.NONE) {
            Entity focused = camera.getFocusedEntity();
            World world = focused.getWorld();
            BlockPos pos = camera.getBlockPos();
            if (Sandstorms.isSandStorming(world, pos)) {
                fogData.fogStart = 4f;
                fogData.fogEnd = 16f;
                fogData.fogShape = FogShape.SPHERE;
            }
        }
    }

}
