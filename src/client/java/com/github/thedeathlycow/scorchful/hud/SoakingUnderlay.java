package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class SoakingUnderlay implements StatusBarOverlayRenderEvents.RenderHealthBarCallback {

    public static final SoakingUnderlay INSTANCE = new SoakingUnderlay();

    public static final ResourceLocation TEXTURE = Scorchful.id("textures/gui/soaking_overlay.png");

    public static final int TEXTURE_WIDTH = 9;
    public static final int TEXTURE_HEIGHT = 10;


    @Override
    public void render(
            GuiGraphics context,
            Player player,
            Vector2i[] heartPositions,
            int displayHealth, int maxDisplayHealth
    ) {
        if (!Scorchful.getConfig().clientConfig.doSoakingOverlay() || !player.thermoo$isWet()) {
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

            int width = isHalfHeart ? 5 : 9;
            context.blit(
                    TEXTURE,
                    pos.x, pos.y - 1,
                    0, 0,
                    width, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }

    }

    private static int getNumBurningPoints(@NotNull Player player, int maxDisplayHealth) {
        float overheatProgress = player.thermoo$getSoakedScale();
        return Mth.ceil(overheatProgress * maxDisplayHealth);
    }

    private static int getNumBurningHeartsFromPoints(int burningPoints) {
        // number of whole hearts
        return Mth.ceil(burningPoints / 2.0f);
    }

    private SoakingUnderlay() {

    }
}
