package com.github.thedeathlycow.scorchful.client.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.client.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.client.config.section.DisplaySettings;
import com.github.thedeathlycow.thermoo.api.client.v1.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.v1.StatusBarOverlayRenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public final class BurningHeartsOverlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {

    public static final BurningHeartsOverlay INSTANCE = new BurningHeartsOverlay();

    public static final Identifier HEART_OVERLAY_TEXTURE = Scorchful.id("textures/gui/fire_heart_overlay.png");

    public static final int TEXTURE_WIDTH = 18;
    public static final int TEXTURE_HEIGHT = 30;

    public boolean drawEngulfedHeart(
            GuiGraphicsExtractor extractor,
            @Nullable Player player,
            int x, int y,
            boolean hardcore, boolean halfHeart
    ) {
        BurningHeartType type = BurningHeartType.forPlayer(player, hardcore);
        if (type != null) {
            extractor.blit(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_OVERLAY_TEXTURE,
                    x, y - 1,
                    halfHeart ? 9 : 0, type.textureV,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );

            return true;
        }
        return false;
    }

    @Override
    public void render(
            GuiGraphicsExtractor context,
            Player player,
            HeartBarContext heartBarContext
    ) {
        DisplaySettings settings = ScorchfulClientConfig.getDisplaySettings();
        if (!settings.enableBurningHeartOverlay() || player.thermoo$isCold()) {
            return;
        }

        final int fireHalfHearts = getNumFireHalfHearts(player, heartBarContext.positions().size());
        final int fireHearts = getNumFireHearts(fireHalfHearts);
        final boolean drawHalfHeartAtEnd = fireHalfHearts % 2 != 0;

        int heartsRendered = 0;

        for (Vector2i position : heartBarContext.positions()) {
            if (heartsRendered >= fireHearts) {
                break;
            }

            int x = position.x();
            int y = position.y() - 1;
            int u = drawHalfHeartAtEnd && heartsRendered == fireHearts - 1 ? 9 : 0;

            context.blit(
                    RenderPipelines.GUI_TEXTURED,
                    HEART_OVERLAY_TEXTURE,
                    x, y,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );

            heartsRendered++;
        }
    }

    static int getNumFireHalfHearts(@NotNull LivingEntity entity, int maxDisplayHealth) {
        float overheatProgress = entity.thermoo$getTemperatureScale();
        return Math.round(overheatProgress * maxDisplayHealth * 2);
    }

    static int getNumFireHearts(int burningPoints) {
        // number of whole hearts
        return Mth.ceil(burningPoints / 2.0f);
    }


    private BurningHeartsOverlay() {
    }
}
