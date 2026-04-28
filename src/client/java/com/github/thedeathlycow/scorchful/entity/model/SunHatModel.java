package com.github.thedeathlycow.scorchful.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class SunHatModel<T extends LivingEntity> extends HumanoidModel<T> {

    public SunHatModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        CubeDeformation dilation = CubeDeformation.NONE;
        MeshDefinition modelData = HumanoidModel.createMesh(dilation, 0.0f);
        PartDefinition root = modelData.getRoot();
        root.addOrReplaceChild(
                PartNames.HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -8.0F, -4.5F, -8.0F,
                                16.0F, 0.0F, 16.0F,
                                dilation.extend(0.1f, 0f, 0.1f)
                        )
                        .texOffs(0, 16)
                        .addBox(
                                -4.0F, -9.0F, -4.0F,
                                8.0F, 4.0F, 8.0F,
                                dilation.extend(0.6f)
                        ),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.head.render(matrices, vertices, light, overlay, color);
    }
}
