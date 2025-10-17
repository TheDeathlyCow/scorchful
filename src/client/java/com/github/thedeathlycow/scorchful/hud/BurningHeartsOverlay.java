package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ClientConfig;
import com.github.thedeathlycow.thermoo.api.client.HeartBarContext;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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
            DrawContext context,
            @Nullable PlayerEntity player,
            int x, int y,
            boolean hardcore, boolean halfHeart
    ) {
        BurningHeartType type = BurningHeartType.forPlayer(player, hardcore);
        if (type != null) {
            context.drawTexture(
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
            DrawContext context,
            PlayerEntity player,
            HeartBarContext heartBarContext
    ) {
        ClientConfig config = ScorchfulConfig.getClientConfig();
        if (!config.doBurningHeartOverlay() || player.thermoo$isCold()) {
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

            context.drawTexture(
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
        return MathHelper.ceil(burningPoints / 2.0f);
    }


    private BurningHeartsOverlay() {
    }
}
