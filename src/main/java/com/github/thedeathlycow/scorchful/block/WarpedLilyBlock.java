package com.github.thedeathlycow.scorchful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WarpedLilyBlock extends Block {

    public static final EnumProperty<WaterSaturationLevel> WATER_SATURATION_LEVEL = EnumProperty.of(
            "water_level", WaterSaturationLevel.class
    );

    public WarpedLilyBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.getStateManager().getDefaultState()
                        .with(WATER_SATURATION_LEVEL, WaterSaturationLevel.DRY)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATER_SATURATION_LEVEL);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(WATER_SATURATION_LEVEL) != WaterSaturationLevel.SOAKED;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
    }
}
