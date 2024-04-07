package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector2i;

public final class MountHealthOverlay implements StatusBarOverlayRenderEvents.RenderMountHealthBarCallback {

    public static final MountHealthOverlay INSTANCE = new MountHealthOverlay();

    @Override
    public void render(
            DrawContext context,
            PlayerEntity player, LivingEntity mount,
            Vector2i[] mountHeartPositions,
            int displayMountHealth, int maxDisplayMountHealth
    ) {
        ScorchfulConfig config = Scorchful.getConfig();
        if (!config.clientConfig.doBurningHeartOverlay() || mount.thermoo$isCold()) {
            return;
        }

        int burningHealthPoints = BurningHeartsOverlay.getNumBurningPoints(mount, maxDisplayMountHealth);
        int burningHealthHearts = BurningHeartsOverlay.getNumBurningHeartsFromPoints(burningHealthPoints);
        for (int i = 0; i < burningHealthHearts; i++) {
            Vector2i pos = mountHeartPositions[i];
            if (pos == null) {
                continue;
            }
            boolean isHalfHeart = i + 1 >= burningHealthHearts && (burningHealthPoints & 1) == 1; // is odd check

            if (isHalfHeart) {
                // flips the half heart around, since animal hearts are backwards
                context.drawTexture(
                        BurningHeartsOverlay.HEART_OVERLAY_TEXTURE,
                        pos.x + 4, pos.y - 1,
                        4, 0,
                        5, 10,
                        BurningHeartsOverlay.TEXTURE_WIDTH, BurningHeartsOverlay.TEXTURE_HEIGHT
                );
            } else {
                context.drawTexture(
                        BurningHeartsOverlay.HEART_OVERLAY_TEXTURE,
                        pos.x, pos.y - 1,
                        0, 0,
                        9, 10,
                        BurningHeartsOverlay.TEXTURE_WIDTH, BurningHeartsOverlay.TEXTURE_HEIGHT
                );
            }
        }
    }

    private MountHealthOverlay() {

    }
}
