package com.github.thedeathlycow.scorchful.client.mixin;

import com.github.thedeathlycow.scorchful.client.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.client.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.entity.effect.FearEffect;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SMobEffects;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
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
public abstract class GuiMixin {
    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Inject(
            method = "extractArmor",
            at = @At("HEAD")
    )
    private static void extractArmorCheckFear(
            GuiGraphicsExtractor graphics,
            Player player,
            int yLineBase,
            int numHealthRows,
            int healthRowHeight,
            int xLeft,
            CallbackInfo ci,
            @Share("is_feared") LocalBooleanRef isFeared
    ) {
        isFeared.set(player.hasEffect(SMobEffects.FEAR) && ScorchfulClientConfig.getDisplaySettings().enableFearArmorBar());
    }

    @WrapOperation(
            method = "extractArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
            )
    )
    private static void makeArmorRedIfFeared(
            GuiGraphicsExtractor instance,
            RenderPipeline renderPipeline,
            Identifier location,
            int x, int y,
            int width, int height,
            Operation<Void> original,
            @Share("is_feared") LocalBooleanRef isFeared
    ) {
        if (isFeared.get()) {
            instance.blitSprite(renderPipeline, location, x, y, width, height, FearEffect.ARMOR_COLOR);
        } else {
            original.call(instance, renderPipeline, location, x, y, width, height);
        }
    }

    @WrapMethod(
            method = "extractHeart"
    )
    private void extractEngulfedHearts(
            GuiGraphicsExtractor graphics,
            Gui.HeartType type,
            int xo, int yo,
            boolean isHardcore,
            boolean blinks,
            boolean half,
            Operation<Void> original
    ) {
        boolean drewEngulfed = type == Gui.HeartType.NORMAL
                && BurningHeartsOverlay.INSTANCE.extractEngulfedHeart(graphics, this.getCameraPlayer(), xo, yo, isHardcore, half);

        if (!drewEngulfed) {
            original.call(graphics, type, xo, yo, isHardcore, blinks, half);
        }
    }

    @ModifyArg(
            method = "extractCameraOverlays",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;extractTextureOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;F)V",
                    ordinal = 0
            ),
            index = 2
    )
    private float modifyEquipmentOpacity(float alpha, @Local(name = "item") ItemStack stack) {
        if (stack.has(SDataComponentTypes.SUN_HAT_RENDERER)) {
            return ScorchfulClientConfig.getAccessibilitySettings().getSunHatShadeOpacity();
        }

        return alpha;
    }
}
