package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DesertWellVisionController implements DesertVisionController {
    @Override
    public boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canSpawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        return DesertVisionController.super.canSpawn(player, world, pos);
    }

//    private void spawnDesertWellVision(ServerWorld world, BlockPos blockPos) {
//        blockPos = blockPos.down();
//        final BlockState slab = Blocks.SANDSTONE_SLAB.getDefaultState();
//        final BlockState wall = Blocks.SANDSTONE.getDefaultState();
//
//        // base
//        for (int dy = -2; dy <= 0; ++dy) {
//            for (int dx = -2; dx <= 2; ++dx) {
//                for (int dz = -2; dz <= 2; ++dz) {
//                    setBlockState(world, blockPos.add(dx, dy, dz), wall);
//                }
//            }
//        }
//
//        // edges / rim
//        for (int dx = -2; dx <= 2; ++dx) {
//            for (int dz = -2; dz <= 2; ++dz) {
//                if ((dx == -2 || dx == 2 || dz == -2 || dz == 2) && (dx != 0 && dz != 0)) {
//                    setBlockState(world, blockPos.add(dx, 1, dz), wall);
//                }
//            }
//        }
//
//        // slabs on rim
//        setBlockState(world, blockPos.add(2, 1, 0), slab);
//        setBlockState(world, blockPos.add(-2, 1, 0), slab);
//        setBlockState(world, blockPos.add(0, 1, 2), slab);
//        setBlockState(world, blockPos.add(0, 1, -2), slab);
//
//        // little hat
//        for (int j = -1; j <= 1; ++j) {
//            for (int k = -1; k <= 1; ++k) {
//                if (j == 0 && k == 0) {
//                    setBlockState(world, blockPos.add(j, 4, k), wall);
//                } else {
//                    setBlockState(world, blockPos.add(j, 4, k), slab);
//                }
//            }
//        }
//
//        // corner pillars
//        for (int j = 1; j <= 3; ++j) {
//            setBlockState(world, blockPos.add(-1, j, -1), wall);
//            setBlockState(world, blockPos.add(-1, j, 1), wall);
//            setBlockState(world, blockPos.add(1, j, -1), wall);
//            setBlockState(world, blockPos.add(1, j, 1), wall);
//        }
//    }
//
//    private void setBlockState(ServerWorld world, BlockPos pos, BlockState state) {
//
//        if (!world.getBlockState(pos).isReplaceable()) {
//            return;
//        }
//
//        DisplayEntity.BlockDisplayEntity entity = spawnAndRide(EntityType.BLOCK_DISPLAY, world, pos);
//        if (entity != null) {
//            ((BlockDisplayAccess) entity).scorchful$setBlockState(state);
//        }
//    }
//
//    @Nullable
//    private <T extends Entity> T spawnAndRide(EntityType<T> type, ServerWorld world, BlockPos pos) {
//        T entity = type.create(world);
//        if (entity != null) {
//            ScorchfulComponents.DESERT_VISION_CHILD.get(entity).makeDesertVisionChild(this.getCause());
//            ScorchfulComponents.DESERT_VISION_CHILD.sync(entity);
//
//            this.children.add(entity);
//            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
//            if (!world.spawnEntity(entity)) {
//                return null;
//            }
//        }
//        return entity;
//    }
}
