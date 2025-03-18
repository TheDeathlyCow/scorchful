package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.registry.SRegistryKeys;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VisionGenerator {

    private final Map<RegistryKey<Biome>, List<HeatVisionDefinition>> cache = new IdentityHashMap<>();

    @Nullable
    public HeatVisionDefinition chooseVision(ServerWorld serverWorld, BlockPos pos) {
        List<HeatVisionDefinition> controllers = this.getPossibleVisions(serverWorld, serverWorld.getBiome(pos));
        if (controllers.isEmpty()) {
            return null;
        }

        return Weighting.getRandom(serverWorld.getRandom(), controllers).orElseThrow(IllegalStateException::new);
    }

    private List<HeatVisionDefinition> getPossibleVisions(ServerWorld serverWorld, RegistryEntry<Biome> biome) {
        Optional<RegistryKey<Biome>> key = biome.getKey();
        if (key.isEmpty()) {
            return List.of();
        }
        return this.cache.computeIfAbsent(key.get(), k -> this.computeVisionsForBiome(biome));
    }


    private List<HeatVisionDefinition> computeVisionsForBiome(ServerWorld serverWorld, RegistryEntry<Biome> biome) {
        return serverWorld.getRegistryManager().getOrThrow(SRegistryKeys.HEAT_VISION).stream()
                .filter(vision -> vision.biomes().contains(biome))
                .toList();
    }

}
