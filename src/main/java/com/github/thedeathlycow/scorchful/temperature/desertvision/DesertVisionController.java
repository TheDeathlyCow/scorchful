package com.github.thedeathlycow.scorchful.temperature.desertvision;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface DesertVisionController {

    double ACTIVATION_DISTANCE = 8.0;

    boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos);

}
