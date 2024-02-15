package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

public class SandPileBlock extends SnowBlock {


    public SandPileBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // overwrite does basically same this as super, but with different sets of tags
        BlockState blockState = world.getBlockState(pos.down());
        if (blockState.isIn(SBlockTags.SAND_PILE_CANNOT_SURVIVE_ON)) {
            return false;
        }
        if (blockState.isIn(SBlockTags.SAND_PILE_CAN_SURVIVE_ON)) {
            return true;
        }

        return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP)
                || (blockState.isOf(this) && blockState.get(LAYERS) == 8);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // STUB: inherited method will cause this to melt in block light, but sand shouldn't melt
    }
}
