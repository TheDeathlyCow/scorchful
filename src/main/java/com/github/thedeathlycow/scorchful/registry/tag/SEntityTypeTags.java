package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SEntityTypeTags {

    public static final TagKey<EntityType<?>> CRIMSON_LILY_HURTS = of("crimson_lily_hurts");

    public static final TagKey<EntityType<?>> DOES_NOT_SLOW_IN_SANDSTORM = of("does_not_slow_in_sandstorm");

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Scorchful.id(path));
    }

    private SEntityTypeTags() {

    }

}
