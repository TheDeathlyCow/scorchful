package com.github.thedeathlycow.scorchful.entity.renderer;

import com.github.thedeathlycow.scorchful.entity.HeatVisionEntity;
import com.github.thedeathlycow.scorchful.entity.renderer.state.HeatVisionEntityRenderState;
import com.github.thedeathlycow.scorchful.mixin.client.accessor.EntityRenderDispatcherAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
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
        state.rendererID = entity.getRendererID();
    }

    @Override
    public void render(HeatVisionEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(state, matrices, vertexConsumers, light);

        EntityRenderDispatcherAccessor accessor = (EntityRenderDispatcherAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher();
        var renderer = (EntityRenderer<HuskEntity, ZombieEntityRenderState>) accessor.renderers().get(EntityType.HUSK);

        ZombieEntityRenderState s = renderer.createRenderState();

        renderer.render(s, matrices, vertexConsumers, light);
    }
}