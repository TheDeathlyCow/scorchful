package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviders;
import com.github.thedeathlycow.scorchful.registry.tag.SEnvironmentProviderTags;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnvironmentProviderTagGenerator extends FabricTagProvider<EnvironmentProvider> {
    public EnvironmentProviderTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ThermooRegistryKeys.ENVIRONMENT_PROVIDER, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(SEnvironmentProviderTags.TEMPERATURE_MODIFIERS)
                .addOptional(SEnvironmentProviders.APPLY_SHADE_FOR_TIME);

        builder(SEnvironmentProviderTags.NETHER_MODIFIERS)
                .addOptional(SEnvironmentProviders.NETHER_BLOCK_LIGHT);
    }
}