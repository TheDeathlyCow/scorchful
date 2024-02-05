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

    public static final TagKey<Biome> HAS_FEATURE_ROOTED_CRIMSON_LILY = SBiomeTags.register("has_feature/rooted_crimson_lily");

    public static TagKey<Biome> register(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(Scorchful.MODID, id));
    }

}
