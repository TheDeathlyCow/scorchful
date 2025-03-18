package com.github.thedeathlycow.scorchful.entity.feature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SunHatFeatureRenderer<S extends BipedEntityRenderState, M extends BipedEntityModel<S>> extends FeatureRenderer<S, M> {

    private static final Identifier TEXTURE = Scorchful.id("textures/entity/sun_hat.png");

    private final SunHatModel<S> model;

    public SunHatFeatureRenderer(FeatureRendererContext<S, M> context, SunHatModel<S> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            S state,
            float limbAngle,
            float limbDistance
    ) {
        if (state.equippedHeadStack.isOf(SItems.SUN_HAT)) {
            this.getContextModel().copyTransforms(this.model);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE));
            this.model.renderHead(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0xffffffff);
        }
    }
}
