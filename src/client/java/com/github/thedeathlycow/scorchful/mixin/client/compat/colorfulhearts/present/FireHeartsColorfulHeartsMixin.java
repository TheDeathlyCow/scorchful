package com.github.thedeathlycow.scorchful.mixin.client.compat.colorfulhearts.present;

import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import terrails.colorfulhearts.heart.Heart;
import terrails.colorfulhearts.heart.HeartType;
import terrails.colorfulhearts.render.HeartRenderer;

@Environment(EnvType.CLIENT)
@Mixin(value = HeartRenderer.class, remap = false)
public class FireHeartsColorfulHeartsMixin {

    @Inject(method = "renderPlayerHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lterrails/colorfulhearts/heart/Heart;draw(Lnet/minecraft/client/util/math/MatrixStack;IIZZLterrails/colorfulhearts/heart/HeartType;)V",
                    remap = true,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void captureHeartPositions(
            DrawContext guiGraphics,
            PlayerEntity player,
            int x, int y,
            int maxHealth, int currentHealth,
            int displayHealth, int absorption,
            boolean renderHighlight,
            CallbackInfo ci,
            int healthHearts, int displayHealthHearts,
            boolean absorptionSameRow,
            int regenIndex,
            HeartType heartType,
            int index,
            Heart heart,
            int xPos, int yPos,
            boolean highlightHeart
    ) {
        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.OVERFLOWING_BARS_ID)) {
            return;
        }
        BurningHeartsOverlay.INSTANCE.setHeartPos(index, xPos, yPos);
    }

    @Inject(
            method = "renderPlayerHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void drawColdHeartOverlayBar(
            DrawContext drawContext, PlayerEntity player,
            int x, int y,
            int maxHealth, int currentHealth, int displayHealth, int absorption,
            boolean renderHighlight, CallbackInfo ci
    ) {
        if (ScorchfulIntegrations.isModLoaded(ScorchfulIntegrations.OVERFLOWING_BARS_ID)) {
            return;
        }
        BurningHeartsOverlay.INSTANCE.drawHeartOverlayBar(
                drawContext,
                player
        );
    }
}
