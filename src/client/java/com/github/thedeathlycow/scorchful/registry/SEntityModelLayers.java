package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class SEntityModelLayers {
    public static final ModelLayerLocation SUN_HAT = new ModelLayerLocation(Scorchful.id("sun_hat"), "main");
    public static final ModelLayerLocation SUN_HAT_BABY = new ModelLayerLocation(Scorchful.id("sun_hat_baby"), "main");

    public static void registerAll() {
        ModelLayerRegistry.registerModelLayer(SUN_HAT, SunHatModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(SUN_HAT_BABY, SunHatModel::getBabyTexturedModelData);
    }

    private SEntityModelLayers() {

    }

}
