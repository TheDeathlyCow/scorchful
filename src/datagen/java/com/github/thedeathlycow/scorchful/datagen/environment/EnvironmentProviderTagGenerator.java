package com.github.thedeathlycow.scorchful.datagen.environment;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviders;
import com.github.thedeathlycow.scorchful.registry.tag.SEnvironmentProviderTags;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EnvironmentProviderTagGenerator extends FabricTagProvider<EnvironmentProvider> {
    public EnvironmentProviderTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ThermooRegistryKeys.ENVIRONMENT_PROVIDER, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SEnvironmentProviderTags.BASE_TEMPERATURE_MODIFIERS)
                .addOptional(SEnvironmentProviders.ARID_HUMIDITY)
                .addOptional(SEnvironmentProviders.RAINY_HUMIDITY)
                .addOptional(SEnvironmentProviders.SUN_LIGHT);

        getOrCreateTagBuilder(SEnvironmentProviderTags.WARM_MODIFIERS)
                .addOptionalTag(SEnvironmentProviderTags.BASE_TEMPERATURE_MODIFIERS);

        getOrCreateTagBuilder(SEnvironmentProviderTags.SCORCHING_MODIFIERS)
                .addOptionalTag(SEnvironmentProviderTags.BASE_TEMPERATURE_MODIFIERS);

        getOrCreateTagBuilder(SEnvironmentProviderTags.NETHER_MODIFIERS)
                .addOptional(SEnvironmentProviders.NETHER_HUMIDITY)
                .addOptional(SEnvironmentProviders.NETHER_BLOCK_LIGHT);
    }
}