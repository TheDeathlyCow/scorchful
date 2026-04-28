package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.tags.TagKey;

public final class SEnvironmentProviderTags {
    public static final TagKey<EnvironmentProvider> TEMPERATURE_MODIFIERS = of("temperature_modifiers");
    public static final TagKey<EnvironmentProvider> NETHER_MODIFIERS = of("nether_modifiers");

    private static TagKey<EnvironmentProvider> of(String path) {
        return TagKey.create(ThermooRegistryKeys.ENVIRONMENT_PROVIDER, Scorchful.id(path));
    }

    private SEnvironmentProviderTags() {

    }
}