package com.github.thedeathlycow.scorchful.registry.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class SClimateBiomeTags {
    public static final TagKey<Biome> IS_NEVER_WARM = SBiomeTags.register("is_never_warm");

    public static final TagKey<Biome> IS_TEMPERATE = SBiomeTags.register("is_climate/temperate");
    public static final TagKey<Biome> IS_NOT_TEMPERATE = SBiomeTags.register("is_not_climate/temperate");

    public static final TagKey<Biome> IS_WARM = SBiomeTags.register("is_climate/warm");
    public static final TagKey<Biome> IS_NOT_WARM = SBiomeTags.register("is_not_climate/warm");

    public static final TagKey<Biome> IS_SCORCHING = SBiomeTags.register("is_climate/scorching");
    public static final TagKey<Biome> IS_NOT_SCORCHING = SBiomeTags.register("is_not_climate/scorching");

    public static final TagKey<Biome> IS_RAINY = SBiomeTags.register("is_climate/rainy");
    public static final TagKey<Biome> IS_NOT_RAINY = SBiomeTags.register("is_not_climate/rainy");

    public static final TagKey<Biome> IS_ARID = SBiomeTags.register("is_climate/arid");
    public static final TagKey<Biome> IS_NOT_ARID = SBiomeTags.register("is_not_climate/arid");

    public static final TagKey<Biome> IS_HELL = SBiomeTags.register("is_climate/hell");
    public static final TagKey<Biome> IS_NOT_HELL = SBiomeTags.register("is_not_climate/hell");

    public static final TagKey<Biome> IS_HUMID_CAVE = SBiomeTags.register("is_climate/humid_cave");
    public static final TagKey<Biome> IS_NOT_HUMID_CAVE = SBiomeTags.register("is_not_climate/humid_cave");

    private SClimateBiomeTags() {

    }
}