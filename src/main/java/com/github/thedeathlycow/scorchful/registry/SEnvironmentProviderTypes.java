package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.environment.provider.CheckBiomeEnvironmentProvider;
import com.github.thedeathlycow.scorchful.temperature.environment.provider.CheckTimeEnvironmentProvider;
import com.github.thedeathlycow.scorchful.temperature.environment.provider.RelativeHumidityThresholdEnvironmentProvider;
import com.github.thedeathlycow.scorchful.temperature.environment.provider.SeaLevelAltitudeTemperatureEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public final class SEnvironmentProviderTypes {
    public static void initialize() {
        Scorchful.LOGGER.debug("Scorchful environment provider types initialized");

        register("sea_level_altitude_temperature", SeaLevelAltitudeTemperatureEnvironmentProvider.CODEC);
        register("check_biome", CheckBiomeEnvironmentProvider.CODEC);
        register("relative_humidity_threshold", RelativeHumidityThresholdEnvironmentProvider.CODEC);
        register("check_time", CheckTimeEnvironmentProvider.CODEC);
    }

    private static void register(String id, MapCodec<? extends EnvironmentProvider> codec) {
        Registry.register(ThermooBuiltInRegistries.ENVIRONMENT_PROVIDER_TYPE, Scorchful.id(id), codec);
    }

    private SEnvironmentProviderTypes() {

    }
}