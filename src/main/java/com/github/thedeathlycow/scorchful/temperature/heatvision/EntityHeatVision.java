package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

public class EntityHeatVision<E extends Entity> extends HeatVision {


    private final EntityType<E> entityType;

    public EntityHeatVision(TagKey<Biome> allowedBiomes, int weight, EntityType<E> entityType) {
        super(allowedBiomes, weight);
        this.entityType = entityType;
    }

    @Override
    public boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        E entity = entityType.create(world);

        if (entity != null) {
            entity.refreshPositionAndAngles(pos, 0f, 0f);
            this.initializeEntity(entity);
            ScorchfulComponents.ENTITY_DESERT_VISION.get(entity).applyDesertVision(this, player);
            boolean spawned = world.spawnEntity(entity);
            if (spawned) {
                ScorchfulComponents.ENTITY_DESERT_VISION.sync(entity);
            }
            return spawned;
        }

        return false;
    }

    protected void initializeEntity(E entity) {
        entity.setInvulnerable(true);
        entity.setNoGravity(true);
        entity.setSilent(true);
        if (entity instanceof MobEntity mob) {
            mob.setAiDisabled(true);
        }
    }

}
