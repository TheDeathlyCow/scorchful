package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class SRegistryKeys {
    public static final RegistryKey<Registry<HeatVisionDefinition>> HEAT_VISION = createRegistryKey("heat_vision");

    public static void initialize() {
        DynamicRegistries.registerSynced(
                HEAT_VISION,
                HeatVisionDefinition.ELEMENT_CODEC,
                HeatVisionDefinition.NETWORK_CODEC
        );
    }

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Scorchful.id(registryId));
    }

    private SRegistryKeys() {

    }
}