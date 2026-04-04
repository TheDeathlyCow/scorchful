package com.github.thedeathlycow.scorchful.client.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.client.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.client.config.section.DisplaySettings;
import com.github.thedeathlycow.thermoo.api.client.v1.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.v1.StatusBarOverlayRenderEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SoakingUnderlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {

    public static final SoakingUnderlay INSTANCE = new SoakingUnderlay();

    public static final Identifier TEXTURE = Scorchful.id("textures/gui/soaking_overlay.png");

    public static final int TEXTURE_WIDTH = 18;
    public static final int TEXTURE_HEIGHT = 10;


    @Override
    public void render(
            GuiGraphicsExtractor context,
            Player player,
            HeartBarContext heartBarContext
    ) {
        DisplaySettings settings = ScorchfulClientConfig.getDisplaySettings();
        if (!settings.enableSoakingOverlay() || !player.thermoo$isWet()) {
            return;
        }

        final int soakedPoints = getNumSoakingPoints(player, heartBarContext.positions().size());
        final int soakedHearts = getFullSoakedHeartsFromPoints(soakedPoints);
        final boolean drawHalfHeartAtEnd = soakedPoints % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= soakedHearts) {
                break;
            }

            int x = position.x();
            int y = position.y() - 1;
            int u = drawHalfHeartAtEnd && heartsRendered == soakedHearts - 1 ? 9 : 0;

            context.blit(
                    RenderPipelines.GUI_TEXTURED,
                    TEXTURE,
                    x, y,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );

            heartsRendered++;
        }
    }

    static int getNumSoakingPoints(@NotNull Player player, int maxDisplayHealth) {
        float soakedScale = player.thermoo$getSoakedScale();
        return Math.round(soakedScale * maxDisplayHealth * 2);
    }

    static int getFullSoakedHeartsFromPoints(int soakedPoints) {
        // number of whole hearts
        return Mth.ceil(soakedPoints / 2.0f);
    }

    private SoakingUnderlay() {

    }
}
