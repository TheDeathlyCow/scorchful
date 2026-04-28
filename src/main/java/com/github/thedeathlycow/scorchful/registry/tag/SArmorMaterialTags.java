package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorMaterial;

public class SArmorMaterialTags {

    public static final TagKey<ArmorMaterial> HEAT_NEUTRAL = of("heat_neutral");

    private static TagKey<ArmorMaterial> of(String path) {
        return TagKey.create(Registries.ARMOR_MATERIAL, Scorchful.id(path));
    }

    private SArmorMaterialTags() {

    }

}
