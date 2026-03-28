package com.github.thedeathlycow.scorchful.registry;

import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class SCutouts {

    public static void registerCutouts() {
        ChunkSectionLayerMap.putBlock(SBlocks.CRIMSON_LILY, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayerMap.putBlock(SBlocks.WARPED_LILY, ChunkSectionLayer.CUTOUT);
    }

    private SCutouts() {

    }
}
