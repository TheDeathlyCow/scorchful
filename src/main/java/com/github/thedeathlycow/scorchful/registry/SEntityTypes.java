package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SEntityTypes {

    public static void registerAll() {
        // nothing here yet owo
    }

    private static void register(String id, EntityType<?> type) {
        Registry.register(Registries.ENTITY_TYPE, Scorchful.id(id), type);
    }

    private SEntityTypes() {

    }
}
