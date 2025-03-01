package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.registry.tag.TagKey;

public final class SEnvironmentProviderTags {
    public static final TagKey<EnvironmentProvider> BASE_TEMPERATURE_MODIFIERS = of("base_temperature_modifiers");

    public static final TagKey<EnvironmentProvider> WARM_AND_RAINY_MODIFIERS = of("warm_and_rainy_modifiers");

    private static TagKey<EnvironmentProvider> of(String path) {
        return TagKey.of(ThermooRegistryKeys.ENVIRONMENT_PROVIDER, Scorchful.id(path));
    }

    private SEnvironmentProviderTags() {

    }
}