package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public record CheckTimeEnvironmentProvider(
        MinMaxBounds.Ints timeRange,
        Holder<EnvironmentProvider> in,
        Holder<EnvironmentProvider> out
) implements EnvironmentProvider {
    public static final MapCodec<CheckTimeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC
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
    public void buildCurrentComponents(Level world, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        long time = world.getDayTime();
        if (timeRange.matches((int) time)) {
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