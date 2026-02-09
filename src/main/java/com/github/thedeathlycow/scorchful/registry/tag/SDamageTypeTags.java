package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class SDamageTypeTags {

    public static final TagKey<DamageType> FIREBALL = of("fireball");

    private static TagKey<DamageType> of(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, Scorchful.id(path));
    }

    private SDamageTypeTags() {

    }

}
