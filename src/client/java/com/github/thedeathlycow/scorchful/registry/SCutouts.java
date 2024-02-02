package com.github.thedeathlycow.scorchful.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class SCutouts {

    public static void registerCutouts() {
        BlockRenderLayerMap.INSTANCE.putBlock(SBlocks.CRIMSON_LILY, RenderLayer.getCutout());
    }

    private SCutouts() {

    }
}
