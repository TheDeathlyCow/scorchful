package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.entity.DesertVision;
import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import com.github.thedeathlycow.scorchful.registry.SEntityTypes;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VisionSpawner {


    public static void tick(PlayerEntity player) {
        World world = player.getWorld();

        if (world.isClient() || !player.hasStatusEffect(SStatusEffects.HEAT_STROKE)) {
            return;
        }

        if (player.age % 20 == 0 && player.getRandom().nextInt(10) == 0) {
            spawnFakeDesertWell((ServerWorld) world, player.getBlockPos());
        }

    }

    private static void spawnFakeDesertWell(ServerWorld world, BlockPos blockPos) {
        final BlockState sand = Blocks.SAND.getDefaultState();
        final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
        final BlockState wall = Blocks.SANDSTONE.getDefaultState();
        final BlockState fluidInside = Blocks.WATER.getDefaultState();
        var desertVision = SEntityTypes.DESERT_VISION.create(world);
        desertVision.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        world.spawnEntity(desertVision);

        for (int i = -2; i <= 0; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -2; k <= 2; ++k) {
                    setBlockState(desertVision, world, blockPos.add(j, i, k), wall);
                }
            }
        }

        setBlockState(desertVision, world, blockPos, fluidInside);

        for (Direction direction : Direction.Type.HORIZONTAL) {
            setBlockState(desertVision, world, blockPos.offset(direction), fluidInside);
        }

        BlockPos blockPos2 = blockPos.down();
        setBlockState(desertVision, world, blockPos2, sand);

        for (Direction direction2 : Direction.Type.HORIZONTAL) {
            setBlockState(desertVision, world, blockPos2.offset(direction2), sand);
        }

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                if (j == -2 || j == 2 || k == -2 || k == 2) {
                    setBlockState(desertVision, world, blockPos.add(j, 1, k), wall);
                }
            }
        }

        setBlockState(desertVision, world, blockPos.add(2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(-2, 1, 0), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, 2), slab);
        setBlockState(desertVision, world, blockPos.add(0, 1, -2), slab);

        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0) {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), wall);
                } else {
                    setBlockState(desertVision, world, blockPos.add(j, 4, k), slab);
                }
            }
        }

        for (int j = 1; j <= 3; ++j) {
            setBlockState(desertVision, world, blockPos.add(-1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(-1, j, 1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, -1), wall);
            setBlockState(desertVision, world, blockPos.add(1, j, 1), wall);
        }
    }

    private static void setBlockState(DesertVision parent, ServerWorld world, BlockPos pos, BlockState state) {
        var entity = EntityType.BLOCK_DISPLAY.create(world);
        if (entity != null) {
            ((BlockDisplayAccess) entity).scorchful$setBlockState(state);
            entity.startRiding(parent, true);
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(entity);
        }
    }

    private VisionSpawner() {
    }


}
