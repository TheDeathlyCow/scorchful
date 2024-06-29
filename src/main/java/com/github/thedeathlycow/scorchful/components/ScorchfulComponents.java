package com.github.thedeathlycow.scorchful.components;

import com.github.thedeathlycow.scorchful.Scorchful;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.Entity;

public class ScorchfulComponents implements EntityComponentInitializer {

    public static final ComponentKey<PlayerComponent> PLAYER = ComponentRegistry.getOrCreate(
            Scorchful.id("player"),
            PlayerComponent.class
    );

    public static final ComponentKey<DesertVisionChildComponent> DESERT_VISION_CHILD = ComponentRegistry.getOrCreate(
            Scorchful.id("desert_vision_child"),
            DesertVisionChildComponent.class
    );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                PLAYER,
                PlayerComponent::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
        registry.registerFor(
                Entity.class,
                DESERT_VISION_CHILD,
                DesertVisionChildComponent::new
        );
    }
}
