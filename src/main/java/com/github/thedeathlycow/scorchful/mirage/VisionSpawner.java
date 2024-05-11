package com.github.thedeathlycow.scorchful.mirage;

import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VisionSpawner {

    private final BlockState sand = Blocks.SAND.getDefaultState();
    private final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
    private final BlockState wall = Blocks.SANDSTONE.getDefaultState();
    private final BlockState fluidInside = Blocks.WATER.getDefaultState();

    public static void tick(PlayerEntity player) {
        World world = player.getWorld();

        if (world.isClient() || !player.hasStatusEffect(SStatusEffects.HEAT_STROKE)) {
            return;
        }

        if (player.age % 20 == 0 && player.getRandom().nextInt(10) == 0) {
            spawnFakeDesertWell((ServerWorld)world, player.getBlockPos());
        }

    }

    private static void spawnFakeDesertWell(ServerWorld world, BlockPos blockPos) {
        var spawner = new VisionSpawner();

        for (int i = -2; i <= 0; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -2; k <= 2; ++k) {
                    setBlockState(world, blockPos.add(j, i, k), spawner.wall);
                }
            }
        }

        setBlockState(world, blockPos, spawner.fluidInside);

        for (Direction direction : Direction.Type.HORIZONTAL) {
            setBlockState(world, blockPos.offset(direction), spawner.fluidInside);
        }

        BlockPos blockPos2 = blockPos.down();
        setBlockState(world, blockPos2, spawner.sand);

        for (Direction direction2 : Direction.Type.HORIZONTAL) {
            setBlockState(world, blockPos2.offset(direction2), spawner.sand);
        }

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                if (j == -2 || j == 2 || k == -2 || k == 2) {
                    setBlockState(world, blockPos.add(j, 1, k), spawner.wall);
                }
            }
        }

        setBlockState(world, blockPos.add(2, 1, 0), spawner.slab);
        setBlockState(world,blockPos.add(-2, 1, 0), spawner.slab);
        setBlockState(world,blockPos.add(0, 1, 2), spawner.slab);
        setBlockState(world,blockPos.add(0, 1, -2), spawner.slab);

        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) {
                    setBlockState(world, blockPos.add(j, 4, k), spawner.wall);
                } else {
                    setBlockState(world, blockPos.add(j, 4, k), spawner.slab);
                }
            }
        }

        for (int j = 1; j <= 3; ++j) {
            setBlockState(world, blockPos.add(-1, j, -1), spawner.wall);
            setBlockState(world, blockPos.add(-1, j, 1), spawner.wall);
            setBlockState(world, blockPos.add(1, j, -1), spawner.wall);
            setBlockState(world, blockPos.add(1, j, 1), spawner.wall);
        }
    }

    private static void setBlockState(ServerWorld world, BlockPos pos, BlockState state) {
        var entity = EntityType.BLOCK_DISPLAY.create(world);
        if (entity != null) {
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            ((BlockDisplayAccess) entity).scorchful$setBlockState(state);
            world.spawnEntity(entity);
        }
    }

    private VisionSpawner() {
    }


}
