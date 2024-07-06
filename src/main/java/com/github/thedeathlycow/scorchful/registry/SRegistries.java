package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.desertvision.DesertVisionController;
import com.github.thedeathlycow.scorchful.temperature.desertvision.DesertWellVisionController;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SRegistries {

    public static final RegistryKey<Registry<DesertVisionController>> DESERT_VISION_CONTROLLERS_KEY = createRegistryKey(
            "desert_vision_controllers"
    );


    public static final Registry<DesertVisionController> DESERT_VISION_CONTROLLERS =
            FabricRegistryBuilder.createSimple(
                    DESERT_VISION_CONTROLLERS_KEY
            ).buildAndRegister();


    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Scorchful.id(registryId));
    }

    private SRegistries() {

    }
}
