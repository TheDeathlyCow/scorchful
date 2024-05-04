package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.hud.ShadeOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
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

    @Shadow @Nullable
    protected abstract PlayerEntity getCameraPlayer();

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

    @Inject(
            method = "drawHeart",
            at = @At("HEAD"),
            cancellable = true
    )
    private void drawEngulfedHearts(
            DrawContext context,
            InGameHud.HeartType type,
            int x, int y,
            boolean hardcore, boolean blinking, boolean half,
            CallbackInfo ci
    ) {
        if (type != InGameHud.HeartType.NORMAL) {
            return;
        }
        boolean drawn = BurningHeartsOverlay.INSTANCE.drawEngulfedHeart(
                context,
                this.getCameraPlayer(),
                x, y,
                hardcore, half
        );

        if (drawn) {
            ci.cancel();
        }
    }

}
