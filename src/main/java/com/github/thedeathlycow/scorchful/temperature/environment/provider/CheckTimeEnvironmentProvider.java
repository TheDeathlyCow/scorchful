package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.mixin.accessor.LevelAccessor;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record CheckTimeEnvironmentProvider(
        MinMaxBounds.Ints timeRange,
        Holder<EnvironmentProvider> in,
        Holder<EnvironmentProvider> out,
        Optional<Holder<WorldClock>> clock
) implements EnvironmentProvider {
    public static final MapCodec<CheckTimeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC
                            .fieldOf("time_range")
                            .forGetter(CheckTimeEnvironmentProvider::timeRange),
                    EnvironmentProvider.HOLDER_CODEC
                            .fieldOf("in")
                            .forGetter(CheckTimeEnvironmentProvider::in),
                    EnvironmentProvider.HOLDER_CODEC
                            .fieldOf("out")
                            .forGetter(CheckTimeEnvironmentProvider::out),
                    RegistryFixedCodec.create(Registries.WORLD_CLOCK)
                            .optionalFieldOf("clock")
                            .forGetter(CheckTimeEnvironmentProvider::clock)
            ).apply(instance, CheckTimeEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        long time = ((LevelAccessor)level).scorchful$getClockTimeTicks(clock);

        if (timeRange.matches((int) time)) {
            in.value().buildCurrentComponents(level, pos, biome, builder);
        } else {
            out.value().buildCurrentComponents(level, pos, biome, builder);
        }
    }

    @Override
    public MapCodec<CheckTimeEnvironmentProvider> codec() {
        return CODEC;
    }


}