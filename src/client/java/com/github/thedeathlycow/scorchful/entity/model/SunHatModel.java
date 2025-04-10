package com.github.thedeathlycow.scorchful.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class SunHatModel<S extends BipedEntityRenderState> extends BipedEntityModel<S> {

    public SunHatModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = Dilation.NONE;
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0f);
        ModelPartData root = modelData.getRoot();
        root.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -8.0F, -4.5F, -8.0F,
                                16.0F, 0.0F, 16.0F,
                                dilation.add(0.1f, 0f, 0.1f)
                        )
                        .uv(0, 16)
                        .cuboid(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                dilation.add(0.6f)
                        ),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    public final void renderHead(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.getHead().render(matrices, vertices, light, overlay, color);
    }
}
