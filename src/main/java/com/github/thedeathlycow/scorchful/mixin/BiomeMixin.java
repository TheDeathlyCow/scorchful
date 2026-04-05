package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.world.ScorchfulBiome;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

@Mixin(Biome.class)
public abstract class BiomeMixin implements ScorchfulBiome {
    @Unique
    @Nullable
    private WeightedList<MobSpawnSettings.SpawnerData> scorchful$BreezeModifiedSpawnData = null;

    @Override
    @Unique
    @NotNull
    public WeightedList<MobSpawnSettings.SpawnerData> scorchful$scorchfulBreezeModifiedSpawnData(WeightedList<MobSpawnSettings.SpawnerData> baseSpawns) {
        if (this.scorchful$BreezeModifiedSpawnData == null) {
            var modifiedMobs = new ArrayList<>(baseSpawns.unwrap());
            modifiedMobs.add(new Weighted<>(new MobSpawnSettings.SpawnerData(EntityType.BREEZE, 1, 2), 100));
            this.scorchful$BreezeModifiedSpawnData = WeightedList.of(modifiedMobs);
        }

        return this.scorchful$BreezeModifiedSpawnData;
    }
}