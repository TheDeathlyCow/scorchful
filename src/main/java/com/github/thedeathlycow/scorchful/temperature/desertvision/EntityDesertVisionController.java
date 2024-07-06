package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class EntityDesertVisionController<E extends Entity> implements DesertVisionController {


    private final EntityType<E> entityType;

    public EntityDesertVisionController(EntityType<E> entityType) {
        this.entityType = entityType;
    }

    @Override
    public final boolean spawn(PlayerEntity player, ServerWorld world, BlockPos pos) {
        if (!this.canSpawn(player, world, pos)) {
            return false;
        }

        E entity = entityType.create(world);

        if (entity != null) {
            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
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
        if (entity instanceof MobEntity mob) {
            mob.setAiDisabled(true);
        }
    }

}
