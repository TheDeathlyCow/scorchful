package com.github.thedeathlycow.scorchful.temperature.desertvision;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public abstract class DesertVisionController {

    public static final double ACTIVATION_DISTANCE = 8.0;

    private final TagKey<Biome> allowedBiomes;

    protected DesertVisionController(TagKey<Biome> allowedBiomes) {
        this.allowedBiomes = allowedBiomes;
    }

    public abstract boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos);

    public final boolean canApplyToBiome(RegistryEntry<Biome> biome) {
        return biome.isIn(allowedBiomes);
    }

}
