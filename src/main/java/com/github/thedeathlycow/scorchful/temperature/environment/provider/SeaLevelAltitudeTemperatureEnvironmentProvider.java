package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

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
                    ExtraCodecs.POSITIVE_INT
                            .fieldOf("max_elevation")
                            .orElse(Integer.MAX_VALUE)
                            .forGetter(SeaLevelAltitudeTemperatureEnvironmentProvider::maxElevation)
            ).apply(instance, SeaLevelAltitudeTemperatureEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(Level world, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        // assume no sea level
        int distanceToSeaLevel = Integer.MAX_VALUE;

        // generally this should be true, since we always execute on the server
        if (world.getChunkSource() instanceof ServerChunkCache serverChunkManager) {
            int height = pos.getY();
            int seaLevel = serverChunkManager.getGenerator().getSeaLevel();

            distanceToSeaLevel = Math.clamp(height - seaLevel, 0, maxElevation);
        }

        double temperature = temperatureAtSeaLevel.valueInUnit(temperatureDecreasePerBlock.unit());

        if (distanceToSeaLevel > 0) {
            temperature += distanceToSeaLevel * temperatureDecreasePerBlock.value();
        }

        builder.set(
                EnvironmentComponentTypes.TEMPERATURE,
                new TemperatureRecord(temperature, temperatureDecreasePerBlock.unit())
        );
    }

    @Override
    public EnvironmentProviderType<SeaLevelAltitudeTemperatureEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.SEA_LEVEL_ALTITUDE_TEMPERATURE;
    }
}