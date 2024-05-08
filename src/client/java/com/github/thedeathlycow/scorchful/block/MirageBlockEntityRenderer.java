package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.block.mirage.MirageBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class MirageBlockEntityRenderer implements BlockEntityRenderer<MirageBlockEntity> {

    public MirageBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(
            MirageBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {

    }

}
