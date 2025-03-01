package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.CheckBiomeEnvironmentProvider;
import com.github.thedeathlycow.scorchful.temperature.SeaLevelAltitudeTemperatureEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import net.minecraft.registry.Registry;

public final class SEnvironmentProviderTypes {
    public static final EnvironmentProviderType<SeaLevelAltitudeTemperatureEnvironmentProvider> SEA_LEVEL_ALTITUDE_TEMPERATURE = register(
            "sea_level_altitude_temperature",
            new EnvironmentProviderType<>(SeaLevelAltitudeTemperatureEnvironmentProvider.CODEC)
    );

    public static final EnvironmentProviderType<CheckBiomeEnvironmentProvider> CHECK_BIOME = register(
            "check_biome",
            new EnvironmentProviderType<>(CheckBiomeEnvironmentProvider.CODEC)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Scorchful environment provider types initialized");
    }

    private static <T extends EnvironmentProvider> EnvironmentProviderType<T> register(String id, EnvironmentProviderType<T> environmentProviderType) {
        return Registry.register(ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE, Scorchful.id(id), environmentProviderType);
    }


    private SEnvironmentProviderTypes() {

    }
}