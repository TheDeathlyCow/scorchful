package com.github.thedeathlycow.scorchful.hud;

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

    }

    private MountHealthOverlay() {

    }
}
