package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

@SuppressWarnings("deprecation")
public class RootedNyliumBlock extends Block implements Fertilizable {

    public static final MapCodec<RootedNyliumBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Registries.BLOCK.getCodec()
                                    .fieldOf("roots_plant_block")
                                    .forGetter(b -> b.rootsPlantBlock),
                            createSettingsCodec()
                    )
                    .apply(instance, RootedNyliumBlock::new)
    );

    private final Block rootsPlantBlock;

    public RootedNyliumBlock(Block rootPlantBlock, Settings settings) {
        super(settings);
        this.rootsPlantBlock = rootPlantBlock;
    }

    @Override
    protected MapCodec<RootedNyliumBlock> getCodec() {
        return CODEC;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!stayAlive(state, world, pos)) {
            world.setBlockState(pos, SBlocks.ROOTED_NETHERRACK.getDefaultState());
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos.up(), this.rootsPlantBlock.getDefaultState());
    }

    private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        int i = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
        return i < world.getMaxLightLevel();
    }
}
