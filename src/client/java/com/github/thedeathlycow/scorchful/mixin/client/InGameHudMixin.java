package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Nullable
    protected abstract PlayerEntity getCameraPlayer();

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
