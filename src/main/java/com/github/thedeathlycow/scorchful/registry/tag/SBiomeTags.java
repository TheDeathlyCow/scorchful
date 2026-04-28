package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SBiomeTags {

    public static final TagKey<Biome> HUMID_BIOMES = SBiomeTags.register("humid_biomes");

    public static final TagKey<Biome> IS_NEVER_WARM_TEMPERATURE = SBiomeTags.register("temperature/is_never_warm");

    public static final TagKey<Biome> HAS_SAND_STORMS = SBiomeTags.register("has_sand_storms");

    public static final TagKey<Biome> HAS_REGULAR_SAND_STORMS = SBiomeTags.register("has_regular_sand_storms");

    public static final TagKey<Biome> HAS_RED_SAND_STORMS = SBiomeTags.register("has_red_sand_storms");
    public static final TagKey<Biome> HAS_PINK_SAND_STORMS = SBiomeTags.register("has_pink_sand_storms");

    public static final TagKey<Biome> HAS_FEATURE_CRIMSON_LILY_PATCH = SBiomeTags.register("has_feature/crimson_lily_patch");
    public static final TagKey<Biome> HAS_FEATURE_SPARSE_CRIMSON_LILY_PATCH = SBiomeTags.register("has_feature/sparse_crimson_lily_patch");

    public static final TagKey<Biome> HEAT_VISION_HUSK = SBiomeTags.register("heat_vision/husk");
    public static final TagKey<Biome> HEAT_VISION_BOAT = SBiomeTags.register("heat_vision/boat");
    public static final TagKey<Biome> HEAT_VISION_POPPY = SBiomeTags.register("heat_vision/poppy");
    public static final TagKey<Biome> HEAT_VISION_BLUE_ORCHID = SBiomeTags.register("heat_vision/blue_orchid");
    public static final TagKey<Biome> HEAT_VISION_SALMON = SBiomeTags.register("heat_vision/salmon");
    public static final TagKey<Biome> HEAT_VISION_COD = SBiomeTags.register("heat_vision/cod");
    public static final TagKey<Biome> HEAT_VISION_SQUID = SBiomeTags.register("heat_vision/squid");

    static TagKey<Biome> register(String id) {
        return TagKey.create(Registries.BIOME, Scorchful.id(id));
    }

    private SBiomeTags() {

    }

}
