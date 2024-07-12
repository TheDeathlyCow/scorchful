package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class SBiomeTags {

    public static final TagKey<Biome> WARM_BIOMES = SBiomeTags.register("warm_biomes");

    public static final TagKey<Biome> SCORCHING_BIOMES = SBiomeTags.register("scorching_biomes");

    public static final TagKey<Biome> HUMID_BIOMES = SBiomeTags.register("humid_biomes");

    public static final TagKey<Biome> HAS_SAND_STORMS = SBiomeTags.register("has_sand_storms");

    public static final TagKey<Biome> HAS_REGULAR_SAND_STORMS = SBiomeTags.register("has_regular_sand_storms");

    public static final TagKey<Biome> HAS_RED_SAND_STORMS = SBiomeTags.register("has_red_sand_storms");

    public static final TagKey<Biome> HAS_FEATURE_CRIMSON_LILY_PATCH = SBiomeTags.register("has_feature/crimson_lily_patch");
    public static final TagKey<Biome> HAS_FEATURE_SPARSE_CRIMSON_LILY_PATCH = SBiomeTags.register("has_feature/sparse_crimson_lily_patch");

    public static final TagKey<Biome> HEAT_VISION_HUSK = SBiomeTags.register("heat_vision/husk");
    public static final TagKey<Biome> HEAT_VISION_BOAT = SBiomeTags.register("heat_vision/boat");
    public static final TagKey<Biome> HEAT_VISION_POPPY = SBiomeTags.register("heat_vision/poppy");
    public static final TagKey<Biome> HEAT_VISION_BLUE_ORCHID = SBiomeTags.register("heat_vision/blue_orchid");
    public static final TagKey<Biome> HEAT_VISION_SALMON = SBiomeTags.register("heat_vision/salmon");
    public static final TagKey<Biome> HEAT_VISION_COD = SBiomeTags.register("heat_vision/cod");
    public static final TagKey<Biome> HEAT_VISION_SQUID = SBiomeTags.register("heat_vision/squid");

    public static TagKey<Biome> register(String id) {
        return TagKey.of(RegistryKeys.BIOME, Scorchful.id(id));
    }

    private SBiomeTags() {

    }

}
