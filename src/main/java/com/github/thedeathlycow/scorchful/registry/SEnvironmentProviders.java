package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.registry.RegistryKey;

public final class SEnvironmentProviders {
    public static final RegistryKey<EnvironmentProvider> SUN_LIGHT = of("sun_light");
    public static final RegistryKey<EnvironmentProvider> NETHER_HUMIDITY = of("nether_humidity");
    public static final RegistryKey<EnvironmentProvider> NETHER_BLOCK_LIGHT = of("nether_block_light");
    public static final RegistryKey<EnvironmentProvider> ARID_HUMIDITY = of("arid_humidity");
    public static final RegistryKey<EnvironmentProvider> RAINY_HUMIDITY = of("rainy_humidity");


    private static RegistryKey<EnvironmentProvider> of(String name) {
        return RegistryKey.of(ThermooRegistryKeys.ENVIRONMENT_PROVIDER, Scorchful.id(name));
    }

    private SEnvironmentProviders() {

    }
}