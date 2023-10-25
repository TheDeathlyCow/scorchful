package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SItemTags {

    public static final TagKey<Item> QUENCHING_FOODS = of("quenching_foods");

    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, Scorchful.id(path));
    }

    private SItemTags() {

    }

}
