package com.github.thedeathlycow.scorchful.entity.feature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.item.SunHatItem;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class SunHatFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation TEXTURE = Scorchful.id("textures/entity/sun_hat.png");

    private final SunHatModel<T> model;

    public SunHatFeatureRenderer(RenderLayerParent<T, M> context, SunHatModel<T> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(
            PoseStack matrices, MultiBufferSource vertexConsumers,
            int light,
            T entity,
            float limbAngle, float limbDistance,
            float tickDelta, float animationProgress,
            float headYaw, float headPitch
    ) {
        if (SunHatItem.isWearingSunHat(entity)) {
            this.getParentModel().copyPropertiesTo(this.model);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(TEXTURE));
            this.model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 0xffffffff);
        }
    }
}
