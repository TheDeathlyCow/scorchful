package com.github.thedeathlycow.scorchful.entity.render;

import com.github.thedeathlycow.scorchful.entity.DesertVisionEntity;
import com.github.thedeathlycow.scorchful.entity.DesertVisionType;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HuskEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DesertVisionEntityRenderer extends EntityRenderer<DesertVisionEntity> {

    private HuskEntity dummyHusk = null;

    private final HuskEntityRenderer huskEntityRenderer;
    private final BlockRenderManager blockRenderManager;

    public DesertVisionEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
        huskEntityRenderer = new HuskEntityRenderer(ctx);
    }

    @Override
    public boolean shouldRender(DesertVisionEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z) && this.isCauseMainPlayer(entity);
    }

    @Override
    public Identifier getTexture(DesertVisionEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public void render(
            DesertVisionEntity entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        DesertVisionType visionType = entity.getVisionType();
        if (visionType == null) {
            return;
        }

        switch (visionType) {
            case HUSK -> {
                huskEntityRenderer.render(
                        getDummyHusk(entity.getWorld()),
                        yaw,
                        tickDelta,
                        matrices,
                        vertexConsumers,
                        light
                );
            }
            case POPPY -> {
                this.blockRenderManager.renderBlockAsEntity(
                        Blocks.POPPY.getDefaultState(),
                        matrices,
                        vertexConsumers,
                        light,
                        OverlayTexture.DEFAULT_UV
                );
            }
        }
    }

    private HuskEntity getDummyHusk(World world) {
        if (this.dummyHusk == null || dummyHusk.getWorld() != world) {
            this.dummyHusk = new HuskEntity(EntityType.HUSK, world);
        }

        return this.dummyHusk;
    }

    private boolean isCauseMainPlayer(DesertVisionEntity entity) {
        ClientPlayerEntity mainPlayer = MinecraftClient.getInstance().player;
        PlayerEntity cause = entity.getCause();

        return mainPlayer != null && cause != null && mainPlayer.getUuid().equals(cause.getUuid());
    }
}
