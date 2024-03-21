package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.hud.ShadeOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;


    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I",
                    shift = At.Shift.BEFORE
            )
    )
    private void renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
        ShadeOverlay.renderShadeOverlay(
                context,
                client.player,
                (ctx, opacity) -> this.renderOverlay(ctx, ShadeOverlay.SHADE_OVERLAY, opacity)
        );
    }

}
