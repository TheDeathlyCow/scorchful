package com.github.thedeathlycow.scorchful.world;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public interface ScorchfulBiome {
    @NotNull
    default WeightedList<MobSpawnSettings.SpawnerData> scorchful$scorchfulBreezeModifiedSpawnData(WeightedList<MobSpawnSettings.SpawnerData> baseSpawns) {
        throw new NotImplementedException("Implemented in mixin");
    }
}