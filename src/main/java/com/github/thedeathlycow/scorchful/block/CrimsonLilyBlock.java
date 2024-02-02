package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class CrimsonLilyBlock extends Block {

    public static final IntProperty WATER_SATURATION_LEVEL = IntProperty.of(
            "water_level", 0, 3
    );

    private static final VoxelShape SHAPE = Block.createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 3.0, 14.0
    );


    public CrimsonLilyBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.getStateManager().getDefaultState()
                        .with(WATER_SATURATION_LEVEL, 0)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATER_SATURATION_LEVEL);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(WATER_SATURATION_LEVEL) != 3;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int saturationLevel = state.get(WATER_SATURATION_LEVEL);

        if (saturationLevel != 3) {
            return;
        }

        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + (random.nextDouble() / 4.0);
        double z = pos.getZ() + random.nextDouble();

        ParticleEffect particle = random.nextFloat() < 0.25f
                ? ParticleTypes.DRIPPING_DRIPSTONE_WATER
                : ParticleTypes.DRIPPING_WATER;

        world.addParticle(
                particle,
                x, y, z,
                0, 0, 0
        );
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.get(WATER_SATURATION_LEVEL) != 3) {
            return;
        }
        if (!world.isClient) {
            world.playSound(
                    null,
                    pos,
                    SSoundEvents.CRIMSON_LILY_SQUELCH,
                    SoundCategory.BLOCKS
            );

            if (entity instanceof Soakable soakable) {
                soakable.thermoo$addWetTicks(soakable.thermoo$getMaxWetTicks());
            }

            world.setBlockState(pos, state.with(WATER_SATURATION_LEVEL, 0));
        } else {
            Random random = world.getRandom();
            Vec3d center = pos.toCenterPos();

            for (int i = 0; i < 20; i++) {

                double x = center.x + (random.nextDouble() / 3) - (1.0 / 6.0);
                double y = center.y;
                double z = center.z + (random.nextDouble() / 3) - (1.0 / 6.0);

                world.addParticle(
                        SParticleTypes.SPURTING_WATER,
                        x, y, z,
                        0, 100.0, 0
                );
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
