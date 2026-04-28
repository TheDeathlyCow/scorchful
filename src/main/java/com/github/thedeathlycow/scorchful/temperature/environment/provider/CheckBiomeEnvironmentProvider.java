package com.github.thedeathlycow.scorchful.temperature.environment.provider;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public record CheckBiomeEnvironmentProvider(
        HolderSet<Biome> biomes,
        HolderSet<Biome> excludeBiomes,
        Holder<EnvironmentProvider> provider
) implements EnvironmentProvider {
    public static final MapCodec<CheckBiomeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.homogeneousList(Registries.BIOME)
                            .fieldOf("biomes")
                            .forGetter(CheckBiomeEnvironmentProvider::biomes),
                    RegistryCodecs.homogeneousList(Registries.BIOME)
                            .fieldOf("exclude_biomes")
                            .forGetter(CheckBiomeEnvironmentProvider::excludeBiomes),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("provider")
                            .forGetter(CheckBiomeEnvironmentProvider::provider)
            ).apply(instance, CheckBiomeEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(Level world, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        if (this.biomes.contains(biome) && !this.excludeBiomes.contains(biome)) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<CheckBiomeEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.CHECK_BIOME;
    }
}