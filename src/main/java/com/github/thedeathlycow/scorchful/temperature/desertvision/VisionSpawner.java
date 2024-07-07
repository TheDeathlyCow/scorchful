package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class VisionSpawner {

    private static final VisionGenerator generator = new VisionGenerator();

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
        var controller = generator.chooseVision();
        if (controller != null) {
            BlockPos pos = chooseVisionPos(serverWorld, cause.getBlockPos(), cause.getRandom());
            controller.spawn(cause, serverWorld, pos);
        }
    }

    private static BlockPos chooseVisionPos(ServerWorld serverWorld, BlockPos origin, Random random) {
        // TODO: make the vision not spawn on top of the player
        int x = origin.getX() + random.nextBetween(-16 * 3, 16 * 3);
        int z = origin.getZ() + random.nextBetween(-16 * 3, 16 * 3);
        int y = serverWorld.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        return new BlockPos(x, y, z);
    }


    private VisionSpawner() {
    }


}
