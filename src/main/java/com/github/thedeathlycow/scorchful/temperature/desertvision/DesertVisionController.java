package com.github.thedeathlycow.scorchful.temperature.desertvision;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface DesertVisionController {

    boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos);

    default boolean canSpawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        return true;
    }

}
