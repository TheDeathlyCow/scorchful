package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticleEffect;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CrimsonLilyBlock extends NetherLilyBlock {

    public CrimsonLilyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.get(WATER_SATURATION_LEVEL) != 3) {
            return;
        }
        if (!world.isClient) {
            soakEntity(state, world, pos, entity);
        } else {
            createSplash(world, pos);
        }
    }

    private static void soakEntity(BlockState state, World world, BlockPos pos, Entity entity) {
        world.playSound(
                null,
                pos,
                SSoundEvents.CRIMSON_LILY_SQUELCH,
                SoundCategory.BLOCKS
        );

        if (entity instanceof Soakable soakable) {
            soakable.thermoo$addWetTicks(soakable.thermoo$getMaxWetTicks());
        }

        if (entity.getType().isIn(SEntityTypeTags.CRIMSON_LILY_HURTS)) {
            entity.damage(
                    world.getDamageSources().generic(),
                    10f
            );
            entity.playSound(
                    SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                    1f, 1f
            );
        }

        world.setBlockState(pos, state.with(WATER_SATURATION_LEVEL, 0));
    }

    private static void createSplash(World world, BlockPos pos) {
        Random random = world.getRandom();
        Vec3d center = pos.toCenterPos();

        for (int i = 0; i < 40; i++) {

            double x = center.x + (random.nextDouble() / 3) - (1.0 / 6.0);
            double y = center.y;
            double z = center.z + (random.nextDouble() / 3) - (1.0 / 6.0);

            world.addParticle(
                    new SpurtingWaterParticleEffect(i),
                    x, y, z,
                    0, 0, 0
            );
        }
    }

}
