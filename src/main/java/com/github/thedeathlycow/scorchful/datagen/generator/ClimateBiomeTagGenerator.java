package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SClimateBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import java.util.concurrent.CompletableFuture;

public class ClimateBiomeTagGenerator extends FabricTagProvider<Biome> {
    public ClimateBiomeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SBiomeTags.IS_NEVER_WARM_TEMPERATURE);

        tag(SClimateBiomeTags.IS_TEMPERATE)
                .addOptionalTag(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS)
                .addOptionalTag(Scorchful.id("temperature/summer/warm"));

        tag(SClimateBiomeTags.IS_NOT_TEMPERATE)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING);

        tag(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
                .addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
                .add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Scorchful.id("warm_biomes"))
                .addOptionalTag(Scorchful.id("temperature/spring/warm"));

        tag(SClimateBiomeTags.IS_NOT_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS);

        tag(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
                .addOptionalTag(BiomeTags.IS_BADLANDS)
                .addOptionalTag(Scorchful.id("scorching_biomes"))
                .addOptionalTag(Scorchful.id("temperature/spring/scorching"));

        tag(SClimateBiomeTags.IS_NOT_SCORCHING)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(Biomes.STONY_PEAKS);

        tag(SClimateBiomeTags.IS_RAINY)
                .addOptionalTag(SBiomeTags.HUMID_BIOMES)
                .addOptionalTag(ConventionalBiomeTags.IS_WET_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SWAMP)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        tag(SClimateBiomeTags.IS_NOT_RAINY);

        tag(SClimateBiomeTags.IS_ARID)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("frostiful", "dry_biomes"))
                .addOptionalTag(ConventionalBiomeTags.IS_DRY_OVERWORLD);

        tag(SClimateBiomeTags.IS_NOT_ARID);

        tag(SClimateBiomeTags.IS_HELL)
                .addOptionalTag(ConventionalBiomeTags.IS_NETHER);

        tag(SClimateBiomeTags.IS_NOT_HELL)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM);

        tag(SClimateBiomeTags.IS_HUMID_CAVE)
                .addOptionalTag(ConventionalBiomeTags.IS_CAVE);

        tag(SClimateBiomeTags.IS_NOT_HUMID_CAVE)
                .add(Biomes.DEEP_DARK);
    }
}