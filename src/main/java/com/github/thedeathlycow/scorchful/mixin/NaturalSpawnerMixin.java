package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.world.SandstormEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {
    @WrapOperation(
            method = "mobsAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getMobsAt(Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/util/random/WeightedList;"
            )
    )
    private static WeightedList<MobSpawnSettings.SpawnerData> blockBreezeFromSpawningWhenNotRainy(
            ChunkGenerator instance,
            Holder<Biome> biome,
            StructureManager structureManager,
            MobCategory category,
            BlockPos pos,
            Operation<WeightedList<MobSpawnSettings.SpawnerData>> original,
            @Local(argsOnly = true) ServerLevel level
    ) {
        WeightedList<MobSpawnSettings.SpawnerData> mobs = original.call(instance, biome, structureManager, category, pos);

        if (category == MobCategory.MONSTER && SandstormEffects.canBreezesSpawnAt(level, pos, biome)) {
            // TODO: replace with cached value from injected field in biome
            var modifiedMobs = new ArrayList<>(mobs.unwrap());
            modifiedMobs.add(new Weighted<>(new MobSpawnSettings.SpawnerData(EntityType.BREEZE, 1, 3), 100));

            return WeightedList.of(modifiedMobs);
        }

        return mobs;
    }
}