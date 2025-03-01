package com.github.thedeathlycow.scorchful.datagen.environment;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviders;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.LightThresholdLightProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.ReduceConstantEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.ReplaceConstantEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.TropicalSeasonEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.LightType;

import java.util.concurrent.CompletableFuture;

public class EnvironmentProviderGenerator extends FabricDynamicRegistryProvider {
    public EnvironmentProviderGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.add(
                SEnvironmentProviders.RAINY_HUMIDITY,
                TropicalSeasonEnvironmentProvider.builder()
                        .withFallbackSeason(ThermooSeason.TROPICAL_DRY)
                        .addSeasonProvider(
                                ThermooSeason.TROPICAL_DRY,
                                RegistryEntry.of(
                                        ReplaceConstantEnvironmentProvider.create(
                                                ComponentMap.builder()
                                                        .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.5)
                                        )
                                )
                        )
                        .addSeasonProvider(
                                ThermooSeason.TROPICAL_WET,
                                RegistryEntry.of(
                                        ReplaceConstantEnvironmentProvider.create(
                                                ComponentMap.builder()
                                                        .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.85)
                                        )
                                )
                        )
                        .build()
        );

        entries.add(
                SEnvironmentProviders.SUN_LIGHT,
                LightThresholdLightProvider.builder(
                                13,
                                RegistryEntry.of(ReplaceConstantEnvironmentProvider.create(ComponentMap.builder())),
                                RegistryEntry.of(
                                        ReduceConstantEnvironmentProvider.create(
                                                ComponentMap.builder()
                                                        .add(
                                                                EnvironmentComponentTypes.TEMPERATURE,
                                                                new TemperatureRecordComponent(-10, TemperatureUnit.KELVIN)
                                                        )
                                        )
                                )
                        ).withLightType(LightType.SKY)
                        .build()
        );
    }

    @Override
    public String getName() {
        return null;
    }
}