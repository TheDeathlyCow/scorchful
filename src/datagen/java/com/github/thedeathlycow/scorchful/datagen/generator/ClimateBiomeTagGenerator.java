package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SClimateBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

public class ClimateBiomeTagGenerator extends FabricTagProvider<Biome> {
    public ClimateBiomeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        builder(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SBiomeTags.IS_NEVER_WARM_TEMPERATURE);

        builder(SClimateBiomeTags.IS_TEMPERATE)
                .addOptionalTag(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(BiomeKeys.STONY_PEAKS)
                .addOptionalTag(scorchfulKey("temperature/summer/warm"));

        builder(SClimateBiomeTags.IS_NOT_TEMPERATE)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING);

        builder(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
                .addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
                .add(BiomeKeys.MANGROVE_SWAMP)
                .addOptionalTag(scorchfulKey("warm_biomes"))
                .addOptionalTag(scorchfulKey("temperature/spring/warm"));

        builder(SClimateBiomeTags.IS_NOT_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(BiomeKeys.STONY_PEAKS);

        builder(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
                .addOptionalTag(BiomeTags.IS_BADLANDS)
                .addOptionalTag(scorchfulKey("scorching_biomes"))
                .addOptionalTag(scorchfulKey("temperature/spring/scorching"));

        builder(SClimateBiomeTags.IS_NOT_SCORCHING)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(BiomeKeys.STONY_PEAKS);

        builder(SClimateBiomeTags.IS_RAINY)
                .addOptionalTag(SBiomeTags.HUMID_BIOMES)
                .addOptionalTag(ConventionalBiomeTags.IS_WET_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SWAMP)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        builder(SClimateBiomeTags.IS_NOT_RAINY);

        builder(SClimateBiomeTags.IS_ARID)
                .addOptionalTag(frostifulKey("dry_biomes"))
                .addOptionalTag(ConventionalBiomeTags.IS_DRY_OVERWORLD);

        builder(SClimateBiomeTags.IS_NOT_ARID);

        builder(SClimateBiomeTags.IS_HELL)
                .addOptionalTag(ConventionalBiomeTags.IS_NETHER);

        builder(SClimateBiomeTags.IS_NOT_HELL)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM);

        builder(SClimateBiomeTags.IS_HUMID_CAVE)
                .addOptionalTag(ConventionalBiomeTags.IS_CAVE);

        builder(SClimateBiomeTags.IS_NOT_HUMID_CAVE)
                .add(BiomeKeys.DEEP_DARK);

        builder(SBiomeTags.HAS_RED_SAND_STORMS)
                .addOptionalTag(BiomeTags.IS_BADLANDS)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS);

        builder(SBiomeTags.HAS_REGULAR_SAND_STORMS)
                .add(BiomeKeys.DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT);
    }

    private static TagKey<Biome> scorchfulKey(String path) {
        return key(Scorchful.MODID, path);
    }

    private static TagKey<Biome> frostifulKey(String path) {
        return key(ScorchfulIntegrations.FROSTIFUL_ID, path);
    }

    private static TagKey<Biome> key(String id, String path) {
        return TagKey.of(RegistryKeys.BIOME, Identifier.of(id, path));
    }
}