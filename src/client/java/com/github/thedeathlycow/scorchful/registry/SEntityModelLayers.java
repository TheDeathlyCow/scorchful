package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshTransformer;

@Environment(EnvType.CLIENT)
public class SEntityModelLayers {
    private static final String HELMET_LAYER = "helmet";

    public static final ModelLayerLocation GENERIC_SUN_HAT = new ModelLayerLocation(Scorchful.id("generic/sun_hat"), HELMET_LAYER);
    public static final ModelLayerLocation GENERIC_BABY_SUN_HAT = new ModelLayerLocation(Scorchful.id("generic/sun_hat_baby"), HELMET_LAYER);

    public static final ModelLayerLocation HUSK_SUN_HAT = new ModelLayerLocation(Scorchful.id("husk/sun_hat"), HELMET_LAYER);
    public static final ModelLayerLocation HUSK_BABY_SUN_HAT = new ModelLayerLocation(Scorchful.id("husk/sun_hat_baby"), HELMET_LAYER);

    public static void registerAll() {
        var babyDeformation = new CubeDeformation(-0.1F, 0.3F, 0.3F);;

        ModelLayerRegistry.registerModelLayer(GENERIC_SUN_HAT, () -> SunHatModel.createLayer(CubeDeformation.NONE));
        ModelLayerRegistry.registerModelLayer(GENERIC_BABY_SUN_HAT, () -> SunHatModel.createBabyLayer(babyDeformation));

        var huskScale = MeshTransformer.scaling(1.0625f);
        ModelLayerRegistry.registerModelLayer(HUSK_SUN_HAT, () -> SunHatModel.createLayer(CubeDeformation.NONE).apply(huskScale));
        ModelLayerRegistry.registerModelLayer(HUSK_BABY_SUN_HAT, () -> SunHatModel.createBabyLayer(CubeDeformation.NONE));
    }

    private SEntityModelLayers() {

    }

}
