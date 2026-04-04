package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SEntityTypeTags {
    public static final TagKey<EntityType<?>> CRIMSON_LILY_HURTS = create("crimson_lily_hurts");
    public static final TagKey<EntityType<?>> DOES_NOT_SLOW_IN_SANDSTORM = create("does_not_slow_in_sandstorm");
    public static final TagKey<EntityType<?>> IMMUNE_TO_FEAR = create("immune_to_fear");
    public static final TagKey<EntityType<?>> MOBS_THAT_PANT = create("mobs_that_pant");
    public static final TagKey<EntityType<?>> HAS_PLAYER_TEMPERATURE_STATUSES = create("has_player_temperature_statuses");
    public static final TagKey<EntityType<?>> SUFFOCATES_IN_SANDSTORMS = create("suffocates_in_sandstorms");

    private static TagKey<EntityType<?>> create(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Scorchful.id(path));
    }

    private SEntityTypeTags() {

    }

}
