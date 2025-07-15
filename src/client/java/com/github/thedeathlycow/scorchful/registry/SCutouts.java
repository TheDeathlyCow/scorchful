package com.github.thedeathlycow.scorchful.registry;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class SCutouts {

    public static void registerCutouts() {
        BlockRenderLayerMap.putBlock(SBlocks.CRIMSON_LILY, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(SBlocks.WARPED_LILY, BlockRenderLayer.CUTOUT);
    }

    private SCutouts() {

    }
}
