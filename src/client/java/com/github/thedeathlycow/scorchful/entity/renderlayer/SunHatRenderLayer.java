package com.github.thedeathlycow.scorchful.entity.renderlayer;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
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

    private final SunHatModel<S> model;
    private final SunHatModel<S> babyModel;

    public SunHatRenderLayer(
            RenderLayerParent<S, M> context,
            EntityModelSet modelLoader
    ) {
        super(context);
        this.model = new SunHatModel<>(modelLoader.bakeLayer(SEntityModelLayers.SUN_HAT));
        this.babyModel = new SunHatModel<>(modelLoader.bakeLayer(SEntityModelLayers.SUN_HAT_BABY));
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

            M contextModel = this.getParentModel();
            contextModel.root().translateAndRotate(matrices);

            queue.order(1)
                    .submitModel(
                            this.model,
                            state,
                            matrices,
                            RenderTypes.armorCutoutNoCull(TEXTURE),
                            light,
                            OverlayTexture.NO_OVERLAY,
                            -1,
                            null,
                            state.outlineColor,
                            null
                    );

            matrices.popPose();
        }
    }
}
