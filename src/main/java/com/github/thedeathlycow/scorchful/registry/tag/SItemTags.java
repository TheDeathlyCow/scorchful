package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SItemTags {

    public static final TagKey<Item> IS_COOLING_FOOD = of("is_cooling_food");
    public static final TagKey<Item> IS_REFRESHING = of("is_refreshing");

    public static final TagKey<Item> IS_SUSTAINING = of("is_sustaining");

    public static final TagKey<Item> IS_HYDRATING = of("is_hydrating");

    public static final TagKey<Item> IS_PARCHING = of("is_parching");

    public static final TagKey<Item> IS_SUN_PROTECTING_HAT = of("is_sun_protecting_hat");

    public static final TagKey<Item> TURTLE_ARMOR = of("turtle_armor");

    public static final TagKey<Item> VERY_HARMFUL_HEAT_RESISTANCE = of("heat_resistance/very_harmful");
    public static final TagKey<Item> NEUTRAL_HEAT_RESISTANCE = of("heat_resistance/neutral");
    public static final TagKey<Item> PROTECTIVE_HEAT_RESISTANCE = of("heat_resistance/protective");
    public static final TagKey<Item> VERY_PROTECTIVE_HEAT_RESISTANCE = of("heat_resistance/very_protective");
    public static final TagKey<Item> HEAT_RESISTANCE_MODIFIED = of("heat_resistance_modified");

    public static final TagKey<Item> BLOCKS_RAIN_WHEN_HOLDING = of("blocks_rain_when_holding");

    public static final TagKey<Item> COMMON_CACTUS_JUICE = ofCommon("drinks/cactus_juice");


    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, Scorchful.id(path));
    }

    private static TagKey<Item> ofCommon(String path) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", path));
    }

    private SItemTags() {

    }

}
