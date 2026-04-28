package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public class RootedNyliumBlock extends Block implements BonemealableBlock {
    public static final MapCodec<RootedNyliumBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BuiltInRegistries.BLOCK.byNameCodec()
                                    .fieldOf("roots_plant_block")
                                    .forGetter(b -> b.rootsPlantBlock),
                            propertiesCodec()
                    )
                    .apply(instance, RootedNyliumBlock::new)
    );

    private final Block rootsPlantBlock;

    public RootedNyliumBlock(Block rootPlantBlock, Properties settings) {
        super(settings);
        this.rootsPlantBlock = rootPlantBlock;
    }

    @Override
    protected MapCodec<RootedNyliumBlock> codec() {
        return CODEC;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!stayAlive(state, world, pos)) {
            world.setBlockAndUpdate(pos, SBlocks.ROOTED_NETHERRACK.defaultBlockState());
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.setBlockAndUpdate(pos.above(), this.rootsPlantBlock.defaultBlockState());
    }

    private static boolean stayAlive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(blockPos);
        int i = LightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
        return i < world.getMaxLightLevel();
    }
}
