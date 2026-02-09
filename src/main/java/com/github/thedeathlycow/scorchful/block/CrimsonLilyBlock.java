package com.github.thedeathlycow.scorchful.block;

import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticleEffect;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.SStats;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("deprecation")
public class CrimsonLilyBlock extends NetherLilyBlock {
    public CrimsonLilyBlock(NetherLilyBehaviour.NetherLilyBehaviourMap behaviorMap, Properties settings) {
        super(behaviorMap, settings);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if (state.getValue(WATER_SATURATION_LEVEL) != 3) {
            return;
        }
        if (world instanceof ServerLevel serverWorld) {
            soakEntity(state, serverWorld, pos, entity);
        } else {
            createSplash(world, pos);
        }
    }

    private static void soakEntity(BlockState state, ServerLevel world, BlockPos pos, Entity entity) {
        world.playSound(
                null,
                pos,
                SSoundEvents.CRIMSON_LILY_SQUELCH,
                SoundSource.BLOCKS
        );

        if (entity instanceof Soakable soakable) {
            soakable.thermoo$addWetTicks(soakable.thermoo$getMaxWetTicks());
        }

        if (entity instanceof Player player) {
            player.awardStat(SStats.SOAKED_BY_CRIMSON_LILY);
        }

        if (entity.getType().is(SEntityTypeTags.CRIMSON_LILY_HURTS)) {
            entity.hurtServer(
                    world,
                    world.damageSources().generic(),
                    10f
            );
            entity.playSound(
                    SoundEvents.GENERIC_EXTINGUISH_FIRE,
                    1f, 1f
            );
        }

        NetherLilyBlock.setWater(state, world, pos, 0);
    }

    private static void createSplash(Level world, BlockPos pos) {
        RandomSource random = world.getRandom();
        Vec3 center = pos.getCenter();

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
