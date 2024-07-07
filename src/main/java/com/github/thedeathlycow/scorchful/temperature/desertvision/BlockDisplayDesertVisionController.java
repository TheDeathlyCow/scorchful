package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.mixin.BlockDisplayAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class BlockDisplayDesertVisionController extends EntityDesertVisionController<DisplayEntity.BlockDisplayEntity> {

    private final Supplier<BlockState> blockStateProvider;

    public BlockDisplayDesertVisionController(Supplier<BlockState> blockStateProvider) {
        super(EntityType.BLOCK_DISPLAY);
        this.blockStateProvider = blockStateProvider;
    }

    @Override
    public boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos).isReplaceable()) {
            return super.spawn(player, world, pos);
        }
        return false;
    }

    @Override
    protected void initializeEntity(DisplayEntity.BlockDisplayEntity entity) {
        super.initializeEntity(entity);
        ((BlockDisplayAccess) entity).scorchful$setBlockState(blockStateProvider.get());
    }
}