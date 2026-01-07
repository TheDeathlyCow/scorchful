package com.github.thedeathlycow.scorchful.entity.feature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SunHatFeatureRenderer<S extends BipedEntityRenderState, M extends BipedEntityModel<S>> extends FeatureRenderer<S, M> {

    private static final Identifier TEXTURE = Scorchful.id("textures/entity/sun_hat.png");

    private final SunHatModel<S> model;
    private final SunHatModel<S> babyModel;

    public SunHatFeatureRenderer(
            FeatureRendererContext<S, M> context,
            LoadedEntityModels modelLoader
    ) {
        super(context);
        this.model = new SunHatModel<>(modelLoader.getModelPart(SEntityModelLayers.SUN_HAT));
        this.babyModel = new SunHatModel<>(modelLoader.getModelPart(SEntityModelLayers.SUN_HAT_BABY));
    }

    @Override
    public void render(
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            S state,
            float limbAngle,
            float limbDistance
    ) {
        if (((SLivingEntityRenderState) state).scorchful$hasSunHat()) {
            matrices.push();

            M contextModel = this.getContextModel();
            contextModel.getRootPart().applyTransform(matrices);

            queue.getBatchingQueue(1)
                    .submitModel(
                            this.model,
                            state,
                            matrices,
                            RenderLayers.armorCutoutNoCull(TEXTURE),
                            light,
                            OverlayTexture.DEFAULT_UV,
                            -1,
                            null,
                            state.outlineColor,
                            null
                    );

            matrices.pop();
        }
    }
}
