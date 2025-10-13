package com.github.thedeathlycow.scorchful.entity.feature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
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
            OrderedRenderCommandQueue queue,
            int light,
            S state,
            float limbAngle,
            float limbDistance
    ) {
        if (((SLivingEntityRenderState) state).scorchful$hasSunHat()) {
            this.getContextModel().applyTransform(matrices);

            queue.getBatchingQueue(1)
                    .submitModel(
                            this.model,
                            state,
                            matrices,
                            RenderLayer.getArmorCutoutNoCull(TEXTURE),
                            light,
                            OverlayTexture.DEFAULT_UV,
                            -1,
                            null,
                            state.outlineColor,
                            null
                    );
        }
    }
}
