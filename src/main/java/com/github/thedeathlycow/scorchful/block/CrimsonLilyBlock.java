package com.github.thedeathlycow.scorchful.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class CrimsonLilyBlock extends Block {

    public static final EnumProperty<WaterSaturationLevel> WATER_SATURATION_LEVEL = EnumProperty.of(
            "water_level", WaterSaturationLevel.class
    );

    private static final VoxelShape SHAPE = Block.createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 3.0, 14.0
    );


    public CrimsonLilyBlock(Settings settings) {
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

        WaterSaturationLevel saturationLevel = state.get(WATER_SATURATION_LEVEL);
        float splashChance = saturationLevel.getSplashChance();

        for (int i = 0; i < saturationLevel.getParticlesPerDisplayTick(); i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + (random.nextDouble() / 4.0);
            double z = pos.getZ() + random.nextDouble();

            ParticleEffect particle = random.nextFloat() < splashChance
                    ? ParticleTypes.DRIPPING_DRIPSTONE_WATER
                    : ParticleTypes.DRIPPING_WATER;

            world.addParticle(
                    particle,
                    x, y, z,
                    0, 0, 0
            );
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
