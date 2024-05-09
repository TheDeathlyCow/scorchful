package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.block.mirage.MirageBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.gen.feature.DesertWellFeature;

public class MirageBlockEntityRenderer implements BlockEntityRenderer<MirageBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public MirageBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(
            MirageBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {
        matrices.push();
        DesertWellFeature
        this.blockRenderManager.renderBlockAsEntity(
                Blocks.STONE.getDefaultState(),
                matrices,
                vertexConsumers,
                light, overlay
        );
        matrices.pop();
    }

}
