package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class SoakingUnderlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {
    @Override
    public void render(
            DrawContext context,
            PlayerEntity player,
            Vector2i[] heartPositions,
            int displayHealth, int maxDisplayHealth
    ) {
        if (!player.thermoo$isWet()) {
            return;
        }

        int burningHealthPoints = getNumBurningPoints(player, maxDisplayHealth);
        int burningHealthHearts = getNumBurningHeartsFromPoints(burningHealthPoints);
        for (int i = 0; i < burningHealthHearts; i++) {
            Vector2i pos = heartPositions[i];
            if (pos == null) {
                continue;
            }
            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            boolean isHalfHeart = i + 1 >= burningHealthHearts && (burningHealthPoints & 1) == 1; // is odd check

            int u = isHalfHeart ? 9 : 0;
            context.drawTexture(
                    BurningHeartsOverlay.HEART_OVERLAY_TEXTURE,
                    pos.x, pos.y - 1,
                    u, 30,
                    9, 10,
                    BurningHeartsOverlay.TEXTURE_WIDTH, BurningHeartsOverlay.TEXTURE_HEIGHT
            );
        }

    }

    private static int getNumBurningPoints(@NotNull PlayerEntity player, int maxDisplayHealth) {
        float overheatProgress = player.thermoo$getSoakedScale();
        return MathHelper.ceil(overheatProgress * maxDisplayHealth);
    }

    private static int getNumBurningHeartsFromPoints(int burningPoints) {
        // number of whole hearts
        return MathHelper.ceil(burningPoints / 2.0f);
    }
}
