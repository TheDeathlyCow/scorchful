package com.github.thedeathlycow.scorchful.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class SunHatModel<T extends LivingEntity> extends BipedEntityModel<T> {

    public SunHatModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0f);
        ModelPartData root = modelData.getRoot();
        root.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -5.0F, -8.0F, -5.0F,
                                10.0F, 0.0F, 10.0F,
                                Dilation.NONE
                        )
                        .uv(0, 10)
                        .cuboid(
                                -3.0F, -12.0F, -3.0F,
                                6.0F, 4.0F, 6.0F,
                                Dilation.NONE
                        ),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
