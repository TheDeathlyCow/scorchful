package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class SEntityAttributes {
    public static final double BASE_MAX_TEMPERATURE = 45.0;

    public static final RegistryEntry<EntityAttribute> REHYDRATION_EFFICIENCY = register(
            "player.rehydration_efficiency",
            new ClampedEntityAttribute(
                    "attribute.name.scorchful.rehydration_efficiency",
                    0.0,
                    0.0, 1.0
            ).setTracked(true)
    );

    public static final RegistryEntry<EntityAttribute> LUNG_CAPACITY = register(
            "lung_capacity",
            new ClampedEntityAttribute(
                    "attribute.name.scorchful.lung_capacity",
                    0.0,
                    0.0, 1024.0
            ).setTracked(true)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized scorchful entity attributes");

        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> BASE_MAX_TEMPERATURE);
    }

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Scorchful.id(id), attribute);
    }

    private SEntityAttributes() {

    }
}
