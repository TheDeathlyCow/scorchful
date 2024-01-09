package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.model.SunHatModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class SEntityModelLayers {

    public static final EntityModelLayer SUN_HAT = new EntityModelLayer(Scorchful.id("sun_hat"), "main");

    public static void registerAll() {
        EntityModelLayerRegistry.registerModelLayer(SUN_HAT, SunHatModel::getTexturedModelData);
    }

    private SEntityModelLayers() {

    }

}
