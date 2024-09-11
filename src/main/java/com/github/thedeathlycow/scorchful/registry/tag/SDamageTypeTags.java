package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SDamageTypeTags {

    public static final TagKey<DamageType> FIREBALL = of("fireball");

    private static TagKey<DamageType> of(String path) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, Scorchful.id(path));
    }

    private SDamageTypeTags() {

    }

}
