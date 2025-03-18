package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.HeatVisionEntity;
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

    public static final ComponentKey<RehydrationComponent> REHYDRATION = ComponentRegistry.getOrCreate(
            Scorchful.id("rehydration"),
            RehydrationComponent.class
    );

    public static final ComponentKey<HeatVisionEntity.SyncedData> HEAT_VISION_SYNCED_DATA = ComponentRegistry.getOrCreate(
            Scorchful.id("heat_vision_synced_data"),
            HeatVisionEntity.SyncedData.class
    );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                PLAYER_WATER,
                PlayerWaterComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerForPlayers(
                REHYDRATION,
                RehydrationComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerFor(
                HeatVisionEntity.class,
                HEAT_VISION_SYNCED_DATA,
                HeatVisionEntity.SyncedData::new
        );
    }
}
