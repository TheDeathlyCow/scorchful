package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.resources.ResourceKey;

public final class SEnvironmentProviders {
    public static final ResourceKey<EnvironmentProvider> APPLY_SHADE_FOR_TIME = of("modifier/apply_shade_for_time");
    public static final ResourceKey<EnvironmentProvider> NETHER_BLOCK_LIGHT = of("modifier/nether_block_light");

    private static ResourceKey<EnvironmentProvider> of(String name) {
        return ResourceKey.create(ThermooRegistryKeys.ENVIRONMENT_PROVIDER, Scorchful.id(name));
    }

    private SEnvironmentProviders() {

    }
}