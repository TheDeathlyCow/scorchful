package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.registry.SRegistryKeys;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import com.github.thedeathlycow.scorchful.util.SWeighting;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VisionGenerator {
    private final Map<RegistryKey<Biome>, List<RegistryEntry.Reference<HeatVisionDefinition>>> cache = new IdentityHashMap<>();

    @Nullable
    public RegistryEntry.Reference<HeatVisionDefinition> chooseVision(ServerWorld serverWorld, BlockPos pos) {
        List<RegistryEntry.Reference<HeatVisionDefinition>> controllers = this.getPossibleVisions(serverWorld, serverWorld.getBiome(pos));
        if (controllers.isEmpty()) {
            return null;
        }

        RegistryEntry.Reference<HeatVisionDefinition> selected = SWeighting.getRandom(
                serverWorld.getRandom(),
                controllers,
                RegistryEntry::value
        );

        if (selected == null) {
            throw new IllegalStateException("Heat vision not present");
        }
        return selected;
    }

    private List<RegistryEntry.Reference<HeatVisionDefinition>> getPossibleVisions(ServerWorld serverWorld, RegistryEntry<Biome> biome) {
        Optional<RegistryKey<Biome>> key = biome.getKey();
        if (key.isEmpty()) {
            return List.of();
        }
        return this.cache.computeIfAbsent(key.get(), k -> this.computeVisionsForBiome(serverWorld, biome));
    }


    private List<RegistryEntry.Reference<HeatVisionDefinition>> computeVisionsForBiome(ServerWorld serverWorld, RegistryEntry<Biome> biome) {
        return serverWorld.getRegistryManager().getOrThrow(SRegistryKeys.HEAT_VISION)
                .streamEntries()
                .filter(vision -> vision.value().biomes().contains(biome))
                .toList();
    }
}
