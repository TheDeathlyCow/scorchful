package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.DesertVision;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SEntityTypes {

    public static final EntityType<DesertVision> DESERT_VISION = EntityType.Builder.create(
                    DesertVision::new,
                    SpawnGroup.AMBIENT
            )
            .setDimensions(0.6f, 1.95f)
            .maxTrackingRange(4)
            .build();

    public static void registerAll() {
        register("desert_vision", DESERT_VISION);
    }

    private static void register(String id, EntityType<?> type) {
        Registry.register(Registries.ENTITY_TYPE, Scorchful.id(id), type);
    }

    private SEntityTypes() {

    }
}
