package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SArmorMaterialTags {

    public static final TagKey<ArmorMaterial> HEAT_NEUTRAL = of("heat_neutral");

    private static TagKey<ArmorMaterial> of(String path) {
        return TagKey.of(RegistryKeys.ARMOR_MATERIAL, Scorchful.id(path));
    }

    private SArmorMaterialTags() {

    }

}
