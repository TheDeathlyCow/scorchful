package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class SRegistries {
    public static final ResourceKey<Registry<HeatVision>> HEAT_VISION_KEY = createRegistryKey(
            "heat_vision"
    );

    public static final Registry<HeatVision> HEAT_VISION =
            FabricRegistryBuilder.create(
                    HEAT_VISION_KEY
            ).buildAndRegister();

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String registryId) {
        return ResourceKey.createRegistryKey(Scorchful.id(registryId));
    }

    private SRegistries() {

    }
}
