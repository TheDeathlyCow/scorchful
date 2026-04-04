package com.github.thedeathlycow.scorchful.client.mixin;

import com.github.thedeathlycow.scorchful.client.ShaderEffectRenderCallback;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private CrossFrameResourcePool resourcePool;

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",
                    shift = At.Shift.AFTER
            ),
            method = "render"
    )
    private void hookShaderRender(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
        ShaderEffectRenderCallback.EVENT.invoker().renderShaderEffects(
                this.minecraft,
                this.resourcePool,
                tickCounter
        );
    }

}