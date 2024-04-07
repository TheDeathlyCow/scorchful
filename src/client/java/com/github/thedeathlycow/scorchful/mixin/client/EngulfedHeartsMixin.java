package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.hud.BurningHeartType;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class EngulfedHeartsMixin {

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

}