package com.github.thedeathlycow.scorchful.mixin.client.compat.overflowingbars.absent;

import com.github.thedeathlycow.scorchful.hud.BurningHeartType;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public class FireHeartsMixin {

	@Nullable
	@Unique
	private BurningHeartType scorchful$heartType;

	@Inject(
			method = "renderHealthBar",
			at = @At("HEAD")
	)
	private void checkBurningHeartType(
			DrawContext context,
			PlayerEntity player,
			int x, int y,
			int lines,
			int regeneratingHeartIndex,
			float maxHealth,
			int lastHealth, int health, int absorption,
			boolean blinking, CallbackInfo ci
	) {
		scorchful$heartType = BurningHeartType.forPlayer(player);
	}

	@Inject(
			method = "drawHeart",
			at = @At("HEAD"),
			cancellable = true
	)
	private void drawEngulfedHearts(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		if (type != InGameHud.HeartType.NORMAL || scorchful$heartType == null) {
			return;
		}
		BurningHeartsOverlay.INSTANCE.drawEngulfedHeart(
				context,
				scorchful$heartType,
				x, y,
				halfHeart
		);
		ci.cancel();
	}

	@Inject(
			method = "renderHealthBar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIIZZ)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	private void captureHeartPositions(
			DrawContext context,
			PlayerEntity player,
			int x, int y,
			int lines,
			int regeneratingHeartIndex,
			float maxHealth,
			int lastHealth,
			int health,
			int absorption,
			boolean blinking,
			CallbackInfo ci,
			// local captures
			InGameHud.HeartType heartType,
			int i, int j, int k, int l,
			int m, // index of heart
			int n, int o,
			int p, int q // position of heart to capture
	) {
		BurningHeartsOverlay.INSTANCE.setHeartPos(m, p, q);
	}

	@Inject(
			method = "renderHealthBar",
			at = @At(
					value = "TAIL"
			)
	)
	private void drawColdHeartOverlayBar(
			DrawContext context,
			PlayerEntity player,
			int x, int y,
			int lines,
			int regeneratingHeartIndex,
			float maxHealth,
			int lastHealth,
			int health,
			int absorption,
			boolean blinking,
			CallbackInfo ci
	) {
		BurningHeartsOverlay.INSTANCE.drawHeartOverlayBar(
				context,
				player
		);
	}

}