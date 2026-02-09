package com.github.thedeathlycow.scorchful.registry;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class SCutouts {

    public static void registerCutouts() {
        BlockRenderLayerMap.putBlock(SBlocks.CRIMSON_LILY, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(SBlocks.WARPED_LILY, ChunkSectionLayer.CUTOUT);
    }

    private SCutouts() {

    }
}
