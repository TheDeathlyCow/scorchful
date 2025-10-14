package com.github.thedeathlycow.scorchful.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(EnvType.CLIENT)
public class SunHatModel<S extends BipedEntityRenderState> extends BipedEntityModel<S> {
    public static final ModelTransformer BABY_TRANSFORMER = ModelTransformer.scaling(0.5f);

    public SunHatModel(ModelPart root) {
        super(root);
        this.setVisible(false);
        this.head.visible = true;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0f);
        ModelPartData root = modelData.getRoot();
        root.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -8.0F, -4.5F, -8.0F,
                                16.0F, 0.0F, 16.0F,
                                Dilation.NONE.add(0.1f, 0f, 0.1f)
                        )
                        .uv(0, 16)
                        .cuboid(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                Dilation.NONE.add(0.6f)
                        ),
                ModelTransform.origin(0.0f, 0f, 0.0f)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getBabyTexturedModelData() {
        return getTexturedModelData().transform(BABY_TRANSFORMER);
    }
}
