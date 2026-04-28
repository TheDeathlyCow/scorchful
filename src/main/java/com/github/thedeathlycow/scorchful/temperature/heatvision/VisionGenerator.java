package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.registry.SRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.biome.Biome;

public class VisionGenerator {

    private final Map<ResourceKey<Biome>, List<HeatVision>> cache = new Object2ObjectOpenHashMap<>();

    @Nullable
    public HeatVision chooseVision(ServerLevel serverWorld, BlockPos pos) {
        List<HeatVision> controllers = this.getPossibleVisions(serverWorld.getBiome(pos));
        if (controllers.isEmpty()) {
            return null;
        }

        return WeightedRandom.getRandomItem(serverWorld.getRandom(), controllers).orElseThrow(IllegalStateException::new);
    }

    private List<HeatVision> getPossibleVisions(Holder<Biome> biome) {
        Optional<ResourceKey<Biome>> key = biome.unwrapKey();
        if (key.isEmpty()) {
            return List.of();
        }
        return this.cache.computeIfAbsent(key.get(), k -> this.computeVisionsForBiome(biome));
    }


    private List<HeatVision> computeVisionsForBiome(Holder<Biome> biome) {
        return SRegistries.HEAT_VISION.stream()
                .filter(vision -> vision.canApplyToBiome(biome))
                .toList();
    }

}
