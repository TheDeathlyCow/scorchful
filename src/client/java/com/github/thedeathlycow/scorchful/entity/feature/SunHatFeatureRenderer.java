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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SunHatFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE = Scorchful.id("textures/entity/sun_hat.png");

    private final SunHatModel<T> model;

    public SunHatFeatureRenderer(FeatureRendererContext<T, M> context, SunHatModel<T> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light,
            T entity,
            float limbAngle, float limbDistance,
            float tickDelta, float animationProgress,
            float headYaw, float headPitch
    ) {
        if (SunHatItem.isWearingSunHat(entity)) {
            this.getContextModel().copyBipedStateTo(this.model);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE));
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0xffffffff);
        }
    }
}
