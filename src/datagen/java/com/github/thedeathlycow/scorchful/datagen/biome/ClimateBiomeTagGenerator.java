package com.github.thedeathlycow.scorchful.datagen.biome;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SClimateBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
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
        getOrCreateTagBuilder(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(Scorchful.id("temperature/is_never_warm"));

        getOrCreateTagBuilder(SClimateBiomeTags.IS_TEMPERATE)
                .addOptionalTag(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_HILL)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .add(BiomeKeys.STONY_PEAKS)
                .addOptionalTag(Scorchful.id("temperature/summer/warm"));

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_TEMPERATE)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_WARM)
                .addOptionalTag(Scorchful.id("warm_biomes"))
                .addOptionalTag(Scorchful.id("temperature/spring/warm"))
                .addOptionalTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_WARM)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .add(BiomeKeys.STONY_PEAKS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH)
                .addOptionalTag(SClimateBiomeTags.IS_SCORCHING);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_SCORCHING)
                .addOptionalTag(Scorchful.id("scorching_biomes"))
                .addOptionalTag(Scorchful.id("temperature/spring/scorching"))
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
                .addOptionalTag(BiomeTags.IS_BADLANDS);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_SCORCHING)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .add(BiomeKeys.STONY_PEAKS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_RAINY)
                .addOptionalTag(Scorchful.id("humid_biomes"))
                .addOptionalTag(ConventionalBiomeTags.IS_WET_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SWAMP)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_RAINY)
                .addOptionalTag(SClimateBiomeTags.IS_ARID);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_ARID)
                .addOptionalTag(Identifier.of("frostiful", "dry_biomes"))
                .addOptionalTag(ConventionalBiomeTags.IS_DRY_OVERWORLD);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_ARID)
                .addOptionalTag(SClimateBiomeTags.IS_RAINY);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_HELL)
                .addOptionalTag(ConventionalBiomeTags.IS_NETHER);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_HELL)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM);

        getOrCreateTagBuilder(SClimateBiomeTags.EXTREME_HUMIDITY)
                .addOptionalTag(SClimateBiomeTags.IS_RAINY)
                .addOptionalTag(SClimateBiomeTags.IS_ARID);
    }
}