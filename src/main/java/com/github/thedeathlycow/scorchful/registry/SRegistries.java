package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SRegistries {

    public static final RegistryKey<Registry<HeatVision>> HEAT_VISION_KEY = createRegistryKey(
            "heat_vision"
    );


    public static final Registry<HeatVision> HEAT_VISION =
            FabricRegistryBuilder.createSimple(
                    HEAT_VISION_KEY
            ).buildAndRegister();


    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Scorchful.id(registryId));
    }

    private SRegistries() {

    }
}
