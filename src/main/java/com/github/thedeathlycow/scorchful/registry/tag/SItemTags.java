package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SItemTags {

    public static final TagKey<Item> IS_REFRESHING = of("is_refreshing");

    public static final TagKey<Item> IS_SUSTAINING = of("is_sustaining");

    public static final TagKey<Item> IS_HYDRATING = of("is_hydrating");

    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, Scorchful.id(path));
    }

    private SItemTags() {

    }

}
