package com.github.thedeathlycow.scorchful.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class SCutouts {

    public static void registerCutouts() {
        BlockRenderLayerMap.INSTANCE.putBlock(SBlocks.CRIMSON_LILY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SBlocks.WARPED_LILY, RenderType.cutout());
    }

    private SCutouts() {

    }
}
