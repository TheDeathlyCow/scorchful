package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class SRegistryKeys {
    public static final RegistryKey<Registry<HeatVisionType>> HEAT_VISION_TYPE = createRegistryKey("heat_vision_type");

    public static void initialize() {
        DynamicRegistries.registerSynced(
                HEAT_VISION_TYPE,
                HeatVisionType.CODEC,
                HeatVisionType.NETWORK_CODEC
        );
    }

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(Scorchful.id(registryId));
    }

    private SRegistryKeys() {

    }
}