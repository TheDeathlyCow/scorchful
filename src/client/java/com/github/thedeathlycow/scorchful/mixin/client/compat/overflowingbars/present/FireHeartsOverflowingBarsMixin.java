package com.github.thedeathlycow.scorchful.mixin.client.compat.overflowingbars.present;

import com.github.thedeathlycow.scorchful.hud.BurningHeartType;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import fuzs.overflowingbars.client.handler.HealthBarRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(value = HealthBarRenderer.class, remap = false)
public class FireHeartsOverflowingBarsMixin {

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lfuzs/overflowingbars/client/handler/HealthBarRenderer;renderHeart(Lnet/minecraft/client/gui/DrawContext;Lfuzs/overflowingbars/client/handler/HealthBarRenderer$HeartType;IIZZZ)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    remap = true
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void captureHeartPosition(
            DrawContext guiGraphics,
            PlayerEntity player,
            int posX,
            int posY,
            int heartOffsetByRegen,
            float maxHealth,
            int currentHealth,
            int displayHealth,
            int currentAbsorptionHealth,
            boolean blink,
            CallbackInfo ci,
            boolean hardcore,
            int normalHearts,
            int maxAbsorptionHearts,
            int absorptionHearts,
            int currentHeart,
            int currentPosX,
            int currentPosY
    ) {
        BurningHeartsOverlay.INSTANCE.setHeartPos(currentHeart, currentPosX, currentPosY);
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "TAIL"
            )
    )
    private void renderColdHeartOverlayBar(
            DrawContext guiGraphics,
            PlayerEntity player,
            int posX,
            int posY,
            int heartOffsetByRegen,
            float maxHealth,
            int currentHealth,
            int displayHealth,
            int currentAbsorptionHealth,
            boolean blink,
            CallbackInfo ci
    ) {
        BurningHeartsOverlay.INSTANCE.drawHeartOverlayBar(
                guiGraphics,
                player
        );
    }
}
