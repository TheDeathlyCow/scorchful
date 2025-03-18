package com.github.thedeathlycow.scorchful.entity.renderer;

import com.github.thedeathlycow.scorchful.entity.HeatVisionEntity;
import com.github.thedeathlycow.scorchful.entity.renderer.state.HeatVisionEntityRenderState;
import com.github.thedeathlycow.scorchful.mixin.client.accessor.EntityRenderDispatcherAccessor;
import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;

public class HeatVisionEntityRenderer extends EntityRenderer<HeatVisionEntity, HeatVisionEntityRenderState> {
    public HeatVisionEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public HeatVisionEntityRenderState createRenderState() {
        return new HeatVisionEntityRenderState();
    }

    @Override
    public void updateRenderState(HeatVisionEntity entity, HeatVisionEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.renderType = entity.getRenderType();
        state.entityState = entity.getEntityRenderState();
    }

    @Override
    public void render(HeatVisionEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(state, matrices, vertexConsumers, light);

        switch (state.renderType) {
            case HeatVisionType ENTITY when state.entityState != null -> {
                EntityRenderDispatcherAccessor accessor = (EntityRenderDispatcherAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher();
                var renderer = (EntityRenderer<Entity, EntityRenderState>) accessor.renderers().get(state.entityState.type());

                EntityRenderState s = renderer.createRenderState();
                s.onFire = state.entityState.onFire();

                renderer.render(s, matrices, vertexConsumers, light);
            }
            case BLOCK -> {

            }
            default -> {}
        }
    }
}