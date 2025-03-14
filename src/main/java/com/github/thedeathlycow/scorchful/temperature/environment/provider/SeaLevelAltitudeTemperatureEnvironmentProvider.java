package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public record SeaLevelAltitudeTemperatureEnvironmentProvider(
        TemperatureRecord temperatureAtSeaLevel,
        TemperatureRecord temperatureDecreasePerBlock,
        int maxElevation
) implements EnvironmentProvider {
    public static final MapCodec<SeaLevelAltitudeTemperatureEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TemperatureRecord.CODEC
                            .fieldOf("temperature_at_sea_level")
                            .forGetter(SeaLevelAltitudeTemperatureEnvironmentProvider::temperatureAtSeaLevel),
                    TemperatureRecord.CODEC
                            .fieldOf("temperature_decrease_per_block")
                            .forGetter(SeaLevelAltitudeTemperatureEnvironmentProvider::temperatureDecreasePerBlock),
                    Codecs.POSITIVE_INT
                            .fieldOf("max_elevation")
                            .orElse(Integer.MAX_VALUE)
                            .forGetter(SeaLevelAltitudeTemperatureEnvironmentProvider::maxElevation)
            ).apply(instance, SeaLevelAltitudeTemperatureEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ComponentMap.Builder builder) {
        // assume no sea level
        int distanceToSeaLevel = Integer.MAX_VALUE;

        // generally this should be true, since we always execute on the server
        if (world.getChunkManager() instanceof ServerChunkManager serverChunkManager) {
            int height = pos.getY();
            int seaLevel = serverChunkManager.getChunkGenerator().getSeaLevel();

            distanceToSeaLevel = Math.clamp(height - seaLevel, 0, maxElevation);
        }

        double temperature = temperatureAtSeaLevel.valueInUnit(temperatureDecreasePerBlock.unit());

        if (distanceToSeaLevel > 0) {
            temperature += distanceToSeaLevel * temperatureDecreasePerBlock.value();
        }

        builder.add(
                EnvironmentComponentTypes.TEMPERATURE,
                new TemperatureRecord(temperature, temperatureDecreasePerBlock.unit())
        );
    }

    @Override
    public EnvironmentProviderType<SeaLevelAltitudeTemperatureEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.SEA_LEVEL_ALTITUDE_TEMPERATURE;
    }
}