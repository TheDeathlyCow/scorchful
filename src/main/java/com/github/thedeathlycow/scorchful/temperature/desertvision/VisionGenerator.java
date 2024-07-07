package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.registry.SRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VisionGenerator {

    private final Map<RegistryKey<Biome>, List<DesertVisionController>> cache = new Object2ObjectOpenHashMap<>();

    @Nullable
    public DesertVisionController chooseVision(ServerWorld serverWorld, BlockPos pos) {
        List<DesertVisionController> controllers = this.getPossibleVisions(serverWorld.getBiome(pos));
        if (controllers.isEmpty()) {
            return null;
        }
        int choice = serverWorld.getRandom().nextInt(controllers.size());
        return controllers.get(choice);
    }

    private List<DesertVisionController> getPossibleVisions(RegistryEntry<Biome> biome) {
        Optional<RegistryKey<Biome>> key = biome.getKey();
        if (key.isEmpty()) {
            return List.of();
        }
        return this.cache.computeIfAbsent(key.get(), k -> this.computeVisionsForBiome(biome));
    }


    private List<DesertVisionController> computeVisionsForBiome(RegistryEntry<Biome> biome) {
        return SRegistries.DESERT_VISION_CONTROLLERS.stream()
                .filter(vision -> vision.canApplyToBiome(biome))
                .toList();
    }

}
