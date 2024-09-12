package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.Entity;

public class ScorchfulComponents implements EntityComponentInitializer {

    public static final ComponentKey<PlayerWaterComponent> PLAYER_WATER = ComponentRegistry.getOrCreate(
            Scorchful.id("player_water"),
            PlayerWaterComponent.class
    );

    public static final ComponentKey<EntityDesertVisionComponent> ENTITY_DESERT_VISION = ComponentRegistry.getOrCreate(
            Scorchful.id("entity_desert_vision"),
            EntityDesertVisionComponent.class
    );

    public static final ComponentKey<MesmerizedComponent> MESMERIZED = ComponentRegistry.getOrCreate(
            Scorchful.id("mesmerized"),
            MesmerizedComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                PLAYER_WATER,
                PlayerWaterComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerFor(
                Entity.class,
                ENTITY_DESERT_VISION,
                EntityDesertVisionComponent::new
        );
        registry.registerFor(
                PathAwareEntity.class,
                MESMERIZED,
                MesmerizedComponent::new
        );
        registry.registerFor(
                PlayerEntity.class,
                MESMERIZED,
                MesmerizedComponent::new
        );
    }
}
