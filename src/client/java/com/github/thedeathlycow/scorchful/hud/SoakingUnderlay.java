package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.DisplaySettings;
import com.github.thedeathlycow.thermoo.api.client.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SoakingUnderlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {

    public static final SoakingUnderlay INSTANCE = new SoakingUnderlay();

    public static final Identifier TEXTURE = Scorchful.id("textures/gui/soaking_overlay.png");

    public static final int TEXTURE_WIDTH = 9;
    public static final int TEXTURE_HEIGHT = 10;


    @Override
    public void render(
            DrawContext context,
            PlayerEntity player,
            HeartBarContext heartBarContext
    ) {
        DisplaySettings settings = ScorchfulClientConfig.getDisplaySettings();
        if (!settings.enableSoakingOverlay() || !player.thermoo$isWet()) {
            return;
        }

        final int soakedPoints = getNumSoakingPoints(player, heartBarContext.positions().size());
        final int soakedHearts = getFullSoakedHeartsFromPoints(soakedPoints);
        final boolean drawHalfHeartAtEnd = soakedHearts % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= soakedHearts) {
                break;
            }
            boolean isHalfHeart = drawHalfHeartAtEnd && heartsRendered == soakedHearts - 1;
            int width = isHalfHeart ? 5 : 9;

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    TEXTURE,
                    position.x(), position.y() - 1,
                    0, 0,
                    width, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );

            heartsRendered++;
        }
    }

    private static int getNumSoakingPoints(@NotNull PlayerEntity player, int maxDisplayHealth) {
        float soakedScale = player.thermoo$getSoakedScale();
        return Math.round(soakedScale * maxDisplayHealth * 2);
    }

    private static int getFullSoakedHeartsFromPoints(int soakedPoints) {
        // number of whole hearts
        return MathHelper.ceil(soakedPoints / 2.0f);
    }

    private SoakingUnderlay() {

    }
}
