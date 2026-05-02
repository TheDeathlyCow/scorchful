package com.github.thedeathlycow.scorchful.temperature.heatvision;

import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

public class EntityHeatVision<E extends Entity> extends HeatVision {


    private final EntityType<E> entityType;

    public EntityHeatVision(TagKey<Biome> allowedBiomes, int weight, EntityType<E> entityType) {
        super(allowedBiomes, weight);
        this.entityType = entityType;
    }

    @Override
    public boolean spawn(Player player, ServerLevel world, BlockPos pos) {
        E entity = entityType.create(world);

        if (entity != null) {
            entity.moveTo(pos, 0f, 0f);
            this.initializeEntity(entity);
            entity.getData(ScorchfulEntityAttachments.ENTITY_DESERT_VISION).applyDesertVision(this, player);
            boolean spawned = world.addFreshEntity(entity);
            if (spawned) {
                entity.syncData(ScorchfulEntityAttachments.ENTITY_DESERT_VISION);
            }
            return spawned;
        }

        return false;
    }

    protected void initializeEntity(E entity) {
        entity.setInvulnerable(true);
        entity.setNoGravity(true);
        entity.setSilent(true);
        if (entity instanceof Mob mob) {
            mob.setNoAi(true);
        }
    }

}
