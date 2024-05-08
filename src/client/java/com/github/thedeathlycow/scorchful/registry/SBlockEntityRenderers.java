package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.block.MirageBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SBlockEntityRenderers {

    public static void registerAll() {
        BlockEntityRendererFactories.register(SBlockEntityTypes.MIRAGE, MirageBlockEntityRenderer::new);
    }

    private SBlockEntityRenderers() {

    }
}
