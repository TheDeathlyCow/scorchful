package com.github.thedeathlycow.scorchful.entity.render;

import com.github.thedeathlycow.scorchful.entity.DesertVisionEntity;
import com.github.thedeathlycow.scorchful.entity.DesertVisionType;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HuskEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Objects;

public class DesertVisionEntityRenderer extends EntityRenderer<DesertVisionEntity> {

    private HuskEntity dummyHusk = null;

    private final HuskEntityRenderer huskEntityRenderer;

    public DesertVisionEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        huskEntityRenderer = new HuskEntityRenderer(ctx);
    }

    @Override
    public boolean shouldRender(DesertVisionEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z) && entity.causeHasHeatStroke();
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

        if (entity.getVisionType() == DesertVisionType.HUSK) {
            huskEntityRenderer.render(
                    getDummyHusk(entity.getWorld()),
                    yaw,
                    tickDelta,
                    matrices,
                    vertexConsumers,
                    light
            );
        }
    }

    private HuskEntity getDummyHusk(World world) {
        if (this.dummyHusk == null) {
            this.dummyHusk = new HuskEntity(EntityType.HUSK, world);
        }

        return this.dummyHusk;
    }
}
