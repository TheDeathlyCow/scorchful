package com.github.thedeathlycow.scorchful.client.entity.model;

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
                                deformation.extend(0.1f, 0f, 0.1f)
                        )

                        // head
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                deformation.extend(0.6f)
                        ),
                PartPose.offset(0.0f, 0f, 0.0f)
        );

        return mesh;
    }

    public static MeshDefinition createBabyMesh(CubeDeformation deformation) {
        MeshDefinition adultMesh = createMesh(deformation);
        return adultMesh
                .apply(HumanoidModel.BABY_TRANSFORMER)
                .apply(mesh -> mesh.transformed(pose -> pose.scaled(1.1f).translated(0.0f, 1.2f, 0.0f)));
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        return LayerDefinition.create(createMesh(deformation), 64, 64);
    }

    public static LayerDefinition createBabyLayer(CubeDeformation deformation) {
        return LayerDefinition.create(createBabyMesh(deformation), 64, 64);
    }

    public static LayerDefinition createHuskLayer(CubeDeformation deformation) {
        return SunHatModel.createLayer(deformation)
                .apply(MeshTransformer.scaling(1.0625f))
                .apply(mesh -> mesh.transformed(pose -> pose.translated(0f, 1f, 0f)));
    }

    public static LayerDefinition createZombieVillagerLayer(CubeDeformation deformation) {
        return SunHatModel.createLayer(deformation)
                .apply(mesh -> mesh.transformed(pose -> pose.translated(0f, -0.5f, 0f)));
    }

    public static LayerDefinition createBabyZombieVillagerLayer(CubeDeformation deformation) {
        return SunHatModel.createBabyLayer(deformation)
                .apply(mesh -> mesh.transformed(pose -> pose.translated(0f, -0.5f, 0f)));
    }

    public static LayerDefinition createWitherSkeletonLayer(CubeDeformation deformation) {
        return SunHatModel.createLayer(deformation)
                .apply(MeshTransformer.scaling(1.1f));
    }
}
