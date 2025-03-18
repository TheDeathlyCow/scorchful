package com.github.thedeathlycow.scorchful.entity.renderer;

import com.github.thedeathlycow.scorchful.entity.HeatVisionEntity;
import com.github.thedeathlycow.scorchful.entity.renderer.state.HeatVisionEntityRenderState;
import com.github.thedeathlycow.scorchful.mixin.client.accessor.EntityRenderDispatcherAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class HeatVisionEntityRenderer extends EntityRenderer<HeatVisionEntity, HeatVisionEntityRenderState> {
    private final BlockRenderManager blockRenderManager;

    public HeatVisionEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.blockRenderManager = context.getBlockRenderManager();
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
            case ENTITY -> {
                if (state.entityState != null) {
                    EntityRenderDispatcherAccessor accessor = (EntityRenderDispatcherAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher();
                    var renderer = (EntityRenderer<Entity, EntityRenderState>) accessor.renderers().get(state.entityState.type());

                    EntityRenderState s = renderer.createRenderState();
                    s.onFire = true;

                    renderer.render(s, matrices, vertexConsumers, light);
                }
            }
            case BLOCK -> {
                this.blockRenderManager.renderBlockAsEntity(
                        Blocks.STONE.getDefaultState(),
                        matrices,
                        vertexConsumers,
                        light,
                        OverlayTexture.DEFAULT_UV
                );
            }
            default -> {
                // render nothing when empty
            }
        }
    }
}