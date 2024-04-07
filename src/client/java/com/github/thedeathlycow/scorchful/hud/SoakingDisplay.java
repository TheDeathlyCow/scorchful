package com.github.thedeathlycow.scorchful.hud;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;

public class SoakingDisplay implements HudRenderCallback {

    public static final Identifier TEXTURE = Scorchful.id("textures/gui/soaking_overlay.png");

    public static final int SPRITE_WIDTH = 9;
    public static final int SPRITE_HEIGHT = 9;
    public static final int WATER_COLOR = ColorHelper.Argb.getArgb(255, 66, 66, 245);

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ClientPlayerInteractionManager interactionManager = client.interactionManager;

        if (player == null || interactionManager == null || !client.interactionManager.hasStatusBars()) {
            return;
        }

    }

    public static void renderSoakingAsOverlay(
            DrawContext drawContext,
            PlayerEntity player,
            Vector2i[] armorPositions,
            int diplayArmor,
            int maxDisplayArmor
    ) {


    }





    private static void renderSoakingAsIcon(DrawContext drawContext, ClientPlayerEntity player) {
        float soakingScale = player.thermoo$getSoakedScale();
        if (soakingScale <= 0f) {
            return;
        }

        int x = drawContext.getScaledWindowWidth() / 2 - 101;
        int y = drawContext.getScaledWindowHeight() - 39;


        drawContext.drawTexture(
                TEXTURE,
                x, y,
                0, 0,
                SPRITE_WIDTH, SPRITE_HEIGHT,
                SPRITE_WIDTH * 2, SPRITE_HEIGHT
        );
        drawContext.fill(
                x + 1, y + SPRITE_HEIGHT - 1,
                x + SPRITE_WIDTH - 1,
                y + SPRITE_HEIGHT - MathHelper.ceil(soakingScale * (SPRITE_HEIGHT - 1)),
                WATER_COLOR
        );
        drawContext.drawTexture(
                TEXTURE,
                x, y,
                SPRITE_WIDTH, 0,
                SPRITE_WIDTH, SPRITE_HEIGHT,
                SPRITE_WIDTH * 2, SPRITE_HEIGHT
        );
    }
}
