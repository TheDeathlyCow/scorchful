package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public record RelativeHumidityThresholdEnvironmentProvider(
        double relativeHumidityThreshold,
        RegistryEntry<EnvironmentProvider> above,
        RegistryEntry<EnvironmentProvider> below
) implements EnvironmentProvider {
    public static final MapCodec<RelativeHumidityThresholdEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                RelativeHumidityComponent.CODEC
                        .fieldOf("relative_humidity_threshold")
                        .forGetter(RelativeHumidityThresholdEnvironmentProvider::relativeHumidityThreshold),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("above")
                            .forGetter(RelativeHumidityThresholdEnvironmentProvider::above),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("below")
                            .forGetter(RelativeHumidityThresholdEnvironmentProvider::below)
            ).apply(instance, RelativeHumidityThresholdEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        double relativeHumidity = builder.getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        if (relativeHumidity >= relativeHumidityThreshold) {
            above.value().buildCurrentComponents(world, pos, biome, builder);
        } else {
            below.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<RelativeHumidityThresholdEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.RELATIVE_HUMIDITY_THRESHOLD;
    }
}