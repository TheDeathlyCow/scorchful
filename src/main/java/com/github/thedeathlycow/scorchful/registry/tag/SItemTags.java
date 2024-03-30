package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SItemTags {

    public static final TagKey<Item> IS_REFRESHING = of("is_refreshing");

    public static final TagKey<Item> IS_SUSTAINING = of("is_sustaining");

    public static final TagKey<Item> IS_HYDRATING = of("is_hydrating");

    public static final TagKey<Item> IS_PARCHING = of("is_parching");

    public static final TagKey<Item> HEAT_NEUTRAL = of("heat_resistance/neutral");

    public static final TagKey<Item> HEAT_PROTECTIVE = of("heat_resistance/protective");

    public static final TagKey<Item> HEAT_VERY_PROTECTIVE = of("heat_resistance/very_protective");

    public static final TagKey<Item> HEAT_VERY_HARMFUL = of("heat_resistance/very_harmful");

    public static final TagKey<Item> IS_SUN_PROTECTING_HAT = of("is_sun_protecting_hat");

    public static final TagKey<Item> TURTLE_ARMOR = of("turtle_armor");

    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, Scorchful.id(path));
    }

    private SItemTags() {

    }

}
