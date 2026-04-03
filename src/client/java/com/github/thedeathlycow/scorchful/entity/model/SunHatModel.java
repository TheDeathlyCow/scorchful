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

        this.root.visible = true;
        this.head.visible = true;
    }

    public static MeshDefinition createMesh(CubeDeformation deformation) {
        MeshDefinition mesh = HumanoidModel.createMesh(deformation, 0.0f);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
                PartNames.HEAD,
                CubeListBuilder.create()
                        // brim
                        .texOffs(0, 0)
                        .addBox(
                                -8.0F, -4.5F, -8.0F,
                                16.0F, 0.0F, 16.0F,
                                CubeDeformation.NONE.extend(0.1f, 0f, 0.1f)
                        )

                        // head
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                CubeDeformation.NONE.extend(0.6f)
                        ),
                PartPose.offset(0.0f, 0f, 0.0f)
        );

        return mesh;
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        return LayerDefinition.create(createMesh(deformation), 64, 64);
    }

    public static LayerDefinition createBabyLayer(CubeDeformation deformation) {
        return createLayer(deformation).apply(BABY_TRANSFORMER);
    }
}
