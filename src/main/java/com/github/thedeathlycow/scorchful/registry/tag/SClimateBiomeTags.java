package com.github.thedeathlycow.scorchful.registry.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public final class SClimateBiomeTags {
    public static final TagKey<Biome> IS_NEVER_WARM = SBiomeTags.register("is_never_warm");
    public static final TagKey<Biome> IS_TEMPERATE = SBiomeTags.register("climate/overworld/is_temperate");
    public static final TagKey<Biome> IS_NOT_TEMPERATE = SBiomeTags.register("climate/overworld/is_not_temperate");
    public static final TagKey<Biome> IS_WARM = SBiomeTags.register("climate/overworld/is_warm");
    public static final TagKey<Biome> IS_NOT_WARM = SBiomeTags.register("climate/overworld/is_not_warm");
    public static final TagKey<Biome> IS_SCORCHING = SBiomeTags.register("climate/overworld/is_scorching");
    public static final TagKey<Biome> IS_NOT_SCORCHING = SBiomeTags.register("climate/overworld/is_not_scorching");
    public static final TagKey<Biome> IS_RAINY = SBiomeTags.register("climate/overworld/is_rainy");
    public static final TagKey<Biome> IS_NOT_RAINY = SBiomeTags.register("climate/overworld/is_not_rainy");
    public static final TagKey<Biome> IS_ARID = SBiomeTags.register("climate/overworld/is_arid");
    public static final TagKey<Biome> IS_NOT_ARID = SBiomeTags.register("climate/overworld/is_not_arid");

    private SClimateBiomeTags() {

    }
}