package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Nullable
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

    @ModifyArg(
            method = "renderMiscOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
                    ordinal = 0
            ),
            index = 2
    )
    private float modifyEquipmentOpacity(
            float opacity,
            @Local ItemStack stack
    ) {
        if (stack.contains(SDataComponentTypes.SUN_HAT_RENDERER)) {
            return Scorchful.getConfig().clientConfig.getSunHatShadeOpacity();
        }

        return opacity;
    }
}
