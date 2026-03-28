package com.github.thedeathlycow.scorchful.datagen.generator.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SClimateBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class ClimateBiomeTagGenerator extends FabricTagsProvider<Biome> {
    public ClimateBiomeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SBiomeTags.IS_NEVER_WARM_TEMPERATURE);

        builder(SClimateBiomeTags.IS_TEMPERATE)
                .addOptionalTag(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS)
                .addOptionalTag(scorchfulKey("temperature/summer/warm"));

        builder(SClimateBiomeTags.IS_NOT_TEMPERATE)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING);

        builder(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
                .addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
                .add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(scorchfulKey("warm_biomes"))
                .addOptionalTag(scorchfulKey("temperature/spring/warm"));

        builder(SClimateBiomeTags.IS_NOT_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS);

        builder(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
                .addOptionalTag(BiomeTags.IS_BADLANDS)
                .addOptionalTag(scorchfulKey("scorching_biomes"))
                .addOptionalTag(scorchfulKey("temperature/spring/scorching"));

        builder(SClimateBiomeTags.IS_NOT_SCORCHING)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS);

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
                .add(Biomes.DEEP_DARK);

        builder(SBiomeTags.HAS_RED_SAND_STORMS)
                .addOptionalTag(BiomeTags.IS_BADLANDS)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS);

        builder(SBiomeTags.HAS_REGULAR_SAND_STORMS)
                .add(Biomes.DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT);
    }

    private static TagKey<Biome> scorchfulKey(String path) {
        return key(Scorchful.MODID, path);
    }

    private static TagKey<Biome> frostifulKey(String path) {
        return key(ScorchfulIntegrations.FROSTIFUL_ID, path);
    }

    private static TagKey<Biome> key(String id, String path) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(id, path));
    }
}