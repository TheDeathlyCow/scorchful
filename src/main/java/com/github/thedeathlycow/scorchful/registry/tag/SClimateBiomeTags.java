package com.github.thedeathlycow.scorchful.registry.tag;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public final class SClimateBiomeTags {
    public static final TagKey<Biome> IS_NEVER_WARM = SBiomeTags.register("is_never_warm");
    public static final TagKey<Biome> IS_WARM_OVERWORLD_CLIMATE = SBiomeTags.register("climate/overworld/is_warm");
    public static final TagKey<Biome> IS_NOT_WARM_OVERWORLD_CLIMATE = SBiomeTags.register("climate/overworld/is_not_warm");
    public static final TagKey<Biome> IS_RAINY_OVERWORLD_CLIMATE = SBiomeTags.register("climate/overworld/is_rainy");
    public static final TagKey<Biome> IS_NOT_RAINY_OVERWORLD_CLIMATE = SBiomeTags.register("climate/overworld/is_not_rainy");

    private SClimateBiomeTags() {

    }
}