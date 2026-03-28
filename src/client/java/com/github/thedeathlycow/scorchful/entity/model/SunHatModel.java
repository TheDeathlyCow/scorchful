package com.github.thedeathlycow.scorchful.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@Environment(EnvType.CLIENT)
public class SunHatModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
    public static final MeshTransformer BABY_TRANSFORMER = MeshTransformer.scaling(0.5f);

    public SunHatModel(ModelPart root) {
        super(root);

        for (ModelPart part : this.allParts()) {
            part.visible = false;
        }

        this.head.visible = true;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition root = modelData.getRoot();
        root.addOrReplaceChild(
                PartNames.HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -8.0F, -4.5F, -8.0F,
                                16.0F, 0.0F, 16.0F,
                                CubeDeformation.NONE.extend(0.1f, 0f, 0.1f)
                        )
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                CubeDeformation.NONE.extend(0.6f)
                        ),
                PartPose.offset(0.0f, 0f, 0.0f)
        );

        return LayerDefinition.create(modelData, 64, 64);
    }

    public static LayerDefinition getBabyTexturedModelData() {
        return getTexturedModelData().apply(BABY_TRANSFORMER);
    }
}
