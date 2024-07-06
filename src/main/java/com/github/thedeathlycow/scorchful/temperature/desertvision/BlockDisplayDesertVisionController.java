package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BlockDisplayDesertVisionController extends EntityDesertVisionController<DisplayEntity.BlockDisplayEntity> {

    private final Block block;

    public BlockDisplayDesertVisionController(Block block) {
        super(EntityType.BLOCK_DISPLAY);
        this.block = block;
    }

    @Override
    public boolean canSpawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos).isReplaceable();
    }

    @Override
    protected void initializeEntity(DisplayEntity.BlockDisplayEntity entity) {
        super.initializeEntity(entity);
        ((BlockDisplayAccess) entity).scorchful$setBlockState(block.getDefaultState());
    }
}
