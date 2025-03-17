package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.renderer.HeatVisionEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class SEntityRenderers {
    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful entity renderers");
        EntityRendererRegistry.register(SEntityTypes.HEAT_VISION, HeatVisionEntityRenderer::new);
    }

    private SEntityRenderers() {

    }
}