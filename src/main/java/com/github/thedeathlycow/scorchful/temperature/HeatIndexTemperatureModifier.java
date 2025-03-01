package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class HeatIndexTemperatureModifier implements EnvironmentProvider {
    public static final MapCodec<HeatIndexTemperatureModifier> CODEC = MapCodec.unit(new HeatIndexTemperatureModifier());

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        double relativeHumidity = builder.getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        double temperatureK = builder.getOrDefault(
                EnvironmentComponentTypes.TEMPERATURE,
                TemperatureRecordComponent.DEFAULT
        ).temperature().valueInUnit(TemperatureUnit.KELVIN);

        double temperatureShiftK;
        if (temperatureK < 0.5) {
            temperatureShiftK = 40.0 * (relativeHumidity - 1) + 10;
        } else {
            temperatureShiftK = 20.0 * (relativeHumidity - 1);
        }

        builder.replace(
                EnvironmentComponentTypes.TEMPERATURE,
                new TemperatureRecordComponent(temperatureK + temperatureShiftK, TemperatureUnit.KELVIN)
        );
    }

    @Override
    public EnvironmentProviderType<? extends EnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.HEAT_INDEX_TEMPERATURE_MODIFIER;
    }
}