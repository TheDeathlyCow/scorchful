package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.predicate.NumberRange;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public record CheckTimeEnvironmentProvider(
        NumberRange.IntRange timeRange,
        RegistryEntry<EnvironmentProvider> in,
        RegistryEntry<EnvironmentProvider> out
) implements EnvironmentProvider {
    public static final MapCodec<CheckTimeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    NumberRange.IntRange.CODEC
                            .fieldOf("time_range")
                            .forGetter(CheckTimeEnvironmentProvider::timeRange),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("in")
                            .forGetter(CheckTimeEnvironmentProvider::in),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("out")
                            .forGetter(CheckTimeEnvironmentProvider::out)
            ).apply(instance, CheckTimeEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ComponentMap.Builder builder) {
        long time = world.getTimeOfDay();
        if (timeRange.test((int) time)) {
            in.value().buildCurrentComponents(world, pos, biome, builder);
        } else {
            out.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<CheckTimeEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.CHECK_TIME;
    }
}