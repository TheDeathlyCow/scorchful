package com.github.thedeathlycow.scorchful.temperature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface HeatVisionRenderer {
    void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}