package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Inject(
            method = "renderHeart",
            at = @At("HEAD"),
            cancellable = true
    )
    private void drawEngulfedHearts(
            GuiGraphics context,
            Gui.HeartType type,
            int x, int y,
            boolean hardcore, boolean blinking, boolean half,
            CallbackInfo ci
    ) {
        if (type != Gui.HeartType.NORMAL) {
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

    @ModifyArg(
            method = "renderCameraOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderTextureOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/Identifier;F)V",
                    ordinal = 0
            ),
            index = 2
    )
    private float modifyEquipmentOpacity(
            float opacity,
            @Local ItemStack stack
    ) {
        if (stack.has(SDataComponentTypes.SUN_HAT_RENDERER)) {
            return ScorchfulClientConfig.getAccessibilitySettings().getSunHatShadeOpacity();
        }

        return opacity;
    }
}
