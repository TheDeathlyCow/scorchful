package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SEntityTypeTags {

    public static final TagKey<EntityType<?>> CRIMSON_LILY_HURTS = of("crimson_lily_hurts");

    public static final TagKey<EntityType<?>> DOES_NOT_SLOW_IN_SANDSTORM = of("does_not_slow_in_sandstorm");

    public static final TagKey<EntityType<?>> IMMUNE_TO_FEAR = of("immune_to_fear");

    public static final TagKey<EntityType<?>> MOBS_THAT_PANT = of("mobs_that_pant");
    public static final TagKey<EntityType<?>> HAS_PLAYER_TEMPERATURE_STATUSES = of("has_player_temperature_statuses");


    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Scorchful.id(path));
    }

    private SEntityTypeTags() {

    }

}
