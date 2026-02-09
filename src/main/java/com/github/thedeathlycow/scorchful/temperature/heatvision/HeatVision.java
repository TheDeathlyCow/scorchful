package com.github.thedeathlycow.scorchful.temperature.heatvision;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

public abstract class HeatVision {

    public static final double ACTIVATION_DISTANCE = 4.0;

    private final TagKey<Biome> allowedBiomes;

    private final int weight;

    protected HeatVision(TagKey<Biome> allowedBiomes, int weight) {
        this.allowedBiomes = allowedBiomes;
        this.weight = weight;
    }

    public abstract boolean spawn(Player player, ServerLevel world, BlockPos pos);

    public final boolean canApplyToBiome(Holder<Biome> biome) {
        return biome.is(allowedBiomes);
    }

    public final int getWeight() {
        return this.weight;
    }
}
