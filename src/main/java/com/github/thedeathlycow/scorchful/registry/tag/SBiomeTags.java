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

    public static TagKey<Biome> register(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(Scorchful.MODID, id));
    }

    private SBiomeTags() {

    }

}
