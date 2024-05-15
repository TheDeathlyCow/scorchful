package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.entity.DesertVisionEntity;
import com.github.thedeathlycow.scorchful.entity.DesertVisionType;
import com.github.thedeathlycow.scorchful.registry.SEntityTypes;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VisionSpawner {

    public static void tick(PlayerEntity player) {
        World world = player.getWorld();

        if (world.isClient() || !player.hasStatusEffect(SStatusEffects.HEAT_STROKE)) {
            return;
        }

        if (player.age % 20 == 0 && player.getRandom().nextInt(10) == 0) {
            spawnDesertVision((ServerWorld) world, player);
        }
    }

    private static void spawnDesertVision(ServerWorld serverWorld, PlayerEntity cause) {

        DesertVisionEntity vision = SEntityTypes.DESERT_VISION.create(serverWorld);
        BlockPos pos = cause.getBlockPos();
        vision.setPos(pos.getX(), pos.getY(), pos.getZ());
        DesertVisionType visionType = DesertVisionType.choose(cause.getRandom());
        vision.setVision(cause, visionType);

        serverWorld.spawnEntity(vision);
    }


    private VisionSpawner() {
    }


}
