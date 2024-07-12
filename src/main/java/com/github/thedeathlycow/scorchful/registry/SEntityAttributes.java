package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class SEntityAttributes {

    public static final RegistryEntry<EntityAttribute> REHYDRATION_EFFICIENCY = register(
            "player.rehydration_efficiency",
            new ClampedEntityAttribute(
                    "attribute.name.player.rehydration_efficiency",
                    0.0,
                    0.0, 1.0
            ).setTracked(true)
    );

    public static void initialize() {
        // load this class
    }

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Scorchful.id(id), attribute);
    }

    private SEntityAttributes() {

    }
}
