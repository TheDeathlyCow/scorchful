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

    public static final ModelLayerLocation PIGLIN_SUN_HAT = new ModelLayerLocation(Scorchful.id("piglin/sun_hat"), HELMET_LAYER);
    public static final ModelLayerLocation PIGLIN_BABY_SUN_HAT = new ModelLayerLocation(Scorchful.id("piglin/sun_hat_baby"), HELMET_LAYER);

    public static final ModelLayerLocation ZOMBIE_VILLAGER_SUN_HAT = new ModelLayerLocation(Scorchful.id("zombie_villager/sun_hat"), HELMET_LAYER);
    public static final ModelLayerLocation ZOMBIE_VILLAGER_BABY_SUN_HAT = new ModelLayerLocation(Scorchful.id("zombie_villager/sun_hat_baby"), HELMET_LAYER);

    public static final ModelLayerLocation WITHER_SKELETON_SUN_HAT = new ModelLayerLocation(Scorchful.id("wither_skeleton/sun_hat"), HELMET_LAYER);


    public static void registerAll() {
        var babyDeformation = new CubeDeformation(-0.1f, 0f, 0.3f);

        ModelLayerRegistry.registerModelLayer(GENERIC_SUN_HAT, () -> SunHatModel.createLayer(CubeDeformation.NONE));
        ModelLayerRegistry.registerModelLayer(GENERIC_BABY_SUN_HAT, () -> SunHatModel.createBabyLayer(babyDeformation));

        ModelLayerRegistry.registerModelLayer(HUSK_SUN_HAT, () -> SunHatModel.createHuskLayer(CubeDeformation.NONE));
        ModelLayerRegistry.registerModelLayer(HUSK_BABY_SUN_HAT, () -> SunHatModel.createBabyLayer(babyDeformation));

        ModelLayerRegistry.registerModelLayer(PIGLIN_SUN_HAT, () -> SunHatModel.createLayer(new CubeDeformation(1.02f, 0f, 1.02f)));
        ModelLayerRegistry.registerModelLayer(PIGLIN_BABY_SUN_HAT, () -> SunHatModel.createBabyLayer(new CubeDeformation(1.02f, 0f, 1.04f)));

        ModelLayerRegistry.registerModelLayer(ZOMBIE_VILLAGER_SUN_HAT, () -> SunHatModel.createZombieVillagerLayer(CubeDeformation.NONE));
        ModelLayerRegistry.registerModelLayer(ZOMBIE_VILLAGER_BABY_SUN_HAT, () -> SunHatModel.createBabyZombieVillagerLayer(CubeDeformation.NONE));

        ModelLayerRegistry.registerModelLayer(WITHER_SKELETON_SUN_HAT, () -> SunHatModel.createWitherSkeletonLayer(CubeDeformation.NONE));
        // no baby wither skeletons (yet)
    }

    private SEntityModelLayers() {

    }

}
