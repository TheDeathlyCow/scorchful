package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class SEntityAttributes {
    public static final double BASE_MAX_TEMPERATURE = 45.0;

    public static final Holder<Attribute> REHYDRATION_EFFICIENCY = register(
            "player.rehydration_efficiency",
            new RangedAttribute(
                    "attribute.name.scorchful.rehydration_efficiency",
                    0.0,
                    0.0, 1.0
            ).setSyncable(true)
    );

    public static final Holder<Attribute> LUNG_CAPACITY = register(
            "lung_capacity",
            new RangedAttribute(
                    "attribute.name.scorchful.lung_capacity",
                    0.0,
                    0.0, 1024.0
            ).setSyncable(true)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized scorchful entity attributes");

        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> BASE_MAX_TEMPERATURE);
    }

    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Scorchful.id(id), attribute);
    }

    private SEntityAttributes() {

    }
}
