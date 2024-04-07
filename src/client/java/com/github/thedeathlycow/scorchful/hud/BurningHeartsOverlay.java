package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.ScorchfulClient;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public final class BurningHeartsOverlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {

    public static final BurningHeartsOverlay INSTANCE = new BurningHeartsOverlay();

    public static final Identifier HEART_OVERLAY_TEXTURE = Scorchful.id("textures/gui/fire_heart_overlay.png");

    public static final int TEXTURE_WIDTH = 18;
    public static final int TEXTURE_HEIGHT = 30;

    public void drawEngulfedHeart(
            DrawContext context,
            BurningHeartType type,
            int x, int y,
            boolean halfHeart
    ) {
        context.drawTexture(
                HEART_OVERLAY_TEXTURE,
                x, y - 1,
                halfHeart ? 9 : 0, type.textureV,
                9, 10,
                TEXTURE_WIDTH, TEXTURE_HEIGHT
        );
    }

    @Override
    public void render(
            DrawContext context,
            PlayerEntity player,
            Vector2i[] heartPositions,
            int displayHealth, int maxDisplayHealth
    ) {
        ScorchfulConfig config = Scorchful.getConfig();
        if (!config.clientConfig.doBurningHeartOverlay() || player.thermoo$isCold()) {
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
                    HEART_OVERLAY_TEXTURE,
                    pos.x, pos.y - 1,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

    }

    static int getNumBurningPoints(@NotNull LivingEntity entity, int maxDisplayHealth) {
        float overheatProgress = entity.thermoo$getTemperatureScale();
        return Math.round(overheatProgress * maxDisplayHealth);
    }

    static int getNumBurningHeartsFromPoints(int burningPoints) {
        // number of whole hearts
        return MathHelper.ceil(burningPoints / 2.0f);
    }


    private BurningHeartsOverlay() {
    }
}
