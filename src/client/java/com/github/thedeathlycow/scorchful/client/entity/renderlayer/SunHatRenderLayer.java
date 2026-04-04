package com.github.thedeathlycow.scorchful.client.entity.renderlayer;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.client.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.client.entity.state.SLivingEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SunHatRenderLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private static final Identifier TEXTURE = Scorchful.id("textures/entity/sun_hat.png");
    private static final Identifier BABY_TEXTURE = Scorchful.id("textures/entity/sun_hat_baby.png");

    private final SunHatModel<S> model;
    private final SunHatModel<S> babyModel;

    public SunHatRenderLayer(
            RenderLayerParent<S, M> context,
            EntityModelSet modelLoader,
            ModelLayerLocation layerLocation,
            ModelLayerLocation babyLayerLocation
    ) {
        super(context);
        this.model = new SunHatModel<>(modelLoader.bakeLayer(layerLocation));
        this.babyModel = new SunHatModel<>(modelLoader.bakeLayer(babyLayerLocation));
    }

    @Override
    public void submit(
            PoseStack matrices,
            SubmitNodeCollector queue,
            int light,
            S state,
            float limbAngle,
            float limbDistance
    ) {
        if (((SLivingEntityRenderState) state).scorchful$hasSunHat()) {
            matrices.pushPose();

            if (state.isBaby) {
                this.submitModel(this.babyModel, BABY_TEXTURE, matrices, queue, light, state);
            } else {
                this.submitModel(this.model, TEXTURE, matrices, queue, light, state);
            }

            matrices.popPose();
        }
    }

    private void submitModel(SunHatModel<S> contextModel, Identifier textureId, PoseStack matrices, SubmitNodeCollector queue, int light, S state) {
        contextModel.root().translateAndRotate(matrices);
        queue.order(1)
                .submitModel(
                        contextModel,
                        state,
                        matrices,
                        RenderTypes.armorCutoutNoCull(textureId),
                        light,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        null,
                        state.outlineColor,
                        null
                );
    }

}
