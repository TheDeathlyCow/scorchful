package com.github.thedeathlycow.scorchful.temperature.heatvision;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Weight;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public abstract class HeatVision implements Weighted {

    public static final double ACTIVATION_DISTANCE = 4.0;

    private final TagKey<Biome> allowedBiomes;

    private final Weight weight;

    protected HeatVision(TagKey<Biome> allowedBiomes, int weight) {
        this.allowedBiomes = allowedBiomes;
        this.weight = Weight.of(weight);
    }

    public abstract boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos);

    public final boolean canApplyToBiome(RegistryEntry<Biome> biome) {
        return biome.isIn(allowedBiomes);
    }

    @Override
    public Weight getWeight() {
        return this.weight;
    }
}
