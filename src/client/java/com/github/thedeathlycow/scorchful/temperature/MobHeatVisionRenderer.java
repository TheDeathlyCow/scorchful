package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.mixin.client.accessor.EntityRenderDispatcherAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;

public class MobHeatVisionRenderer implements HeatVisionRenderer {
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        EntityRenderDispatcherAccessor accessor = (EntityRenderDispatcherAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher();
        var renderer = (EntityRenderer<HuskEntity, ZombieEntityRenderState>) accessor.renderers().get(EntityType.HUSK);

        ZombieEntityRenderState state = renderer.createRenderState();

        renderer.render(state, matrices, vertexConsumers, light);
    }
}