package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.registry.SDesertVisionControllers;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class VisionGenerator {

    public DesertVisionController chooseVision(ServerWorld serverWorld, BlockPos pos) {
        return SDesertVisionControllers.BOAT;
    }

}
