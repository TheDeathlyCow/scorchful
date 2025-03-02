package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.registry.SEnvironmentProviderTypes;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProviderType;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public record CheckBiomeEnvironmentProvider(
        RegistryEntryList<Biome> biomes,
        RegistryEntryList<Biome> excludeBiomes,
        RegistryEntry<EnvironmentProvider> provider
) implements EnvironmentProvider {
    public static final MapCodec<CheckBiomeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(CheckBiomeEnvironmentProvider::biomes),
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("exclude_biomes")
                            .forGetter(CheckBiomeEnvironmentProvider::excludeBiomes),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("provider")
                            .forGetter(CheckBiomeEnvironmentProvider::provider)
            ).apply(instance, CheckBiomeEnvironmentProvider::new)
    );

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        if (this.biomes.contains(biome) && !this.excludeBiomes.contains(biome)) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<CheckBiomeEnvironmentProvider> getType() {
        return SEnvironmentProviderTypes.CHECK_BIOME;
    }
}