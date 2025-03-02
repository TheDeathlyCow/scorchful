package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.registry.RegistryKey;

public final class SEnvironmentProviders {
    public static final RegistryKey<EnvironmentProvider> TIME_OF_DAY = of("modifier/time_of_day");
    public static final RegistryKey<EnvironmentProvider> SHADE = of("modifier/shade");
    public static final RegistryKey<EnvironmentProvider> NETHER_BLOCK_LIGHT = of("modifier/nether_block_light");
    public static final RegistryKey<EnvironmentProvider> WEATHER = of("modifier/weather");

    public static final RegistryKey<EnvironmentProvider> NETHER_HUMIDITY = of("set_humidity/hell");
    public static final RegistryKey<EnvironmentProvider> ARID_HUMIDITY = of("set_humidity/arid_climate");
    public static final RegistryKey<EnvironmentProvider> RAINY_HUMIDITY = of("set_humidity/rainy_climate");

    private static RegistryKey<EnvironmentProvider> of(String name) {
        return RegistryKey.of(ThermooRegistryKeys.ENVIRONMENT_PROVIDER, Scorchful.id(name));
    }

    private SEnvironmentProviders() {

    }
}