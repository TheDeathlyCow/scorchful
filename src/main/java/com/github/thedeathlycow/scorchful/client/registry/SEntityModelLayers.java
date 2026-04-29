package com.github.thedeathlycow.scorchful.client.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.client.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class SEntityModelLayers {

    public static final ModelLayerLocation SUN_HAT = new ModelLayerLocation(Scorchful.id("sun_hat"), "main");

    public static void registerAll() {
        EntityModelLayerRegistry.registerModelLayer(SUN_HAT, SunHatModel::getTexturedModelData);
    }

    private SEntityModelLayers() {

    }

}
