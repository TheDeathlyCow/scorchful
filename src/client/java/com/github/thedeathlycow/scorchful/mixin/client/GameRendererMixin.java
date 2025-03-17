package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.client.ShaderEffectRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private Pool pool;

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V",
                    shift = At.Shift.AFTER
            ),
            method = "render"
    )
    private void hookShaderRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        ShaderEffectRenderCallback.EVENT.invoker().renderShaderEffects(
                this.client,
                this.pool,
                tickCounter
        );
    }

}