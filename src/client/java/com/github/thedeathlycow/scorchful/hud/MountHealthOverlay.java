package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.DisplaySettings;
import com.github.thedeathlycow.thermoo.api.client.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;

public final class MountHealthOverlay implements StatusBarOverlayRenderEvents.RenderMountHealthBarCallback {

    public static final MountHealthOverlay INSTANCE = new MountHealthOverlay();

    @Override
    public void render(
            GuiGraphics context,
            Player player, LivingEntity mount,
            HeartBarContext heartBarContext
    ) {
        DisplaySettings settings = ScorchfulClientConfig.getDisplaySettings();
        if (!settings.enableBurningHeartOverlay() || mount.thermoo$isCold()) {
            return;
        }

        final int fireHalfHearts = BurningHeartsOverlay.getNumFireHalfHearts(mount, heartBarContext.positions().size());
        final int fireHearts = BurningHeartsOverlay.getNumFireHearts(fireHalfHearts);
        final boolean drawHalfHeartAtEnd = fireHalfHearts % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= fireHearts) {
                break;
            }

            int x = position.x();
            int y = position.y() - 1;
            boolean isHalfHeart = drawHalfHeartAtEnd && heartsRendered == fireHearts - 1;

            if (isHalfHeart) {
                context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        BurningHeartsOverlay.HEART_OVERLAY_TEXTURE,
                        x + 4, y,
                        4, 0,
                        5, 10,
                        BurningHeartsOverlay.TEXTURE_WIDTH, BurningHeartsOverlay.TEXTURE_HEIGHT
                );
            } else {
                context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        BurningHeartsOverlay.HEART_OVERLAY_TEXTURE,
                        x, y,
                        0, 0,
                        9, 10,
                        BurningHeartsOverlay.TEXTURE_WIDTH, BurningHeartsOverlay.TEXTURE_HEIGHT
                );
            }

            heartsRendered++;
        }
    }

    private MountHealthOverlay() {

    }
}
