package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BurningHeartsOverlay {

    public static final BurningHeartsOverlay INSTANCE = new BurningHeartsOverlay();


    public static final Identifier HEART_OVERLAY_TEXTURE = Scorchful.id("textures/gui/fire_heart_overlay.png");
    public static final int MAX_FIRE_HEARTS = 20;

    private final int[] heartXPositions = new int[MAX_FIRE_HEARTS];
    private final int[] heartYPositions = new int[MAX_FIRE_HEARTS];

    public void setHeartPos(int index, int xPos, int yPos) {
        if (index >= 0 && index < MAX_FIRE_HEARTS) {
            this.heartXPositions[index] = xPos;
            this.heartYPositions[index] = yPos - 1; // -1 due to fire texture being 1px down compared to normal hearts
        }
    }

    public void drawHeartOverlayBar(DrawContext context, PlayerEntity player) {

        ScorchfulConfig config = Scorchful.getConfig();
        if (!config.clientConfig.doBurningHeartOverlay()) {
            return;
        }

        int fireHeartPoints = getNumFirePoints(player);
        int fireHearts = getNumFireHeartsFromPoints(fireHeartPoints);

        for (int m = 0; m < fireHearts; m++) {
            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            int x = heartXPositions[m];
            int y = heartYPositions[m];
            boolean isHalfHeart = m + 1 >= fireHearts && (fireHeartPoints & 1) == 1; // is odd check

            int u = isHalfHeart ? 9 : 0;
            context.drawTexture(HEART_OVERLAY_TEXTURE, x, y, u, 0, 9, 10, 18, 10);
        }
    }

    public int getNumFirePoints(@NotNull PlayerEntity player) {
        float tempScale = player.thermoo$getTemperatureScale();
        if (tempScale <= 0f) {
            return 0;
        }

        final float playerMaxHealth = player.getMaxHealth();

        // number of half cold hearts
        int frozenHealthPoints;
        // match the number of cold hearts to the display if hearts render is altered
//        if (FrostifulIntegrations.isHeartsRenderOverridden()) {
//            // 20 is the (expected) maximum number of health points that the render mod will display
//            frozenHealthPoints = (int) (freezingProgress * Math.min(MAX_COLD_HEARTS, playerMaxHealth));
//        } else {
//        }


        // max cold hearts is multiplied by 2 to covert to points
        frozenHealthPoints = (int) (tempScale * Math.min(MAX_FIRE_HEARTS * 2.0f, playerMaxHealth));

        return frozenHealthPoints;
    }

    public int getNumFireHeartsFromPoints(int fireHealthPoints) {
        // number of whole hearts
        int frozenHealthHearts = MathHelper.ceil(fireHealthPoints / 2.0f);

        return Math.min(MAX_FIRE_HEARTS, frozenHealthHearts);
    }

    private BurningHeartsOverlay() {
    }
}
