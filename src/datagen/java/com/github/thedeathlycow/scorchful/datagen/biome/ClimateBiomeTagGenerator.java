package com.github.thedeathlycow.scorchful.datagen.biome;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SClimateBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
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


        getOrCreateTagBuilder(SClimateBiomeTags.IS_WARM_OVERWORLD_CLIMATE)
                .addOptionalTag(Scorchful.id("warm_biomes"))
                .addOptionalTag(Scorchful.id("temperature/spring/warm"))
                .addOptionalTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_WARM_OVERWORLD_CLIMATE)
                .addOptionalTag(SClimateBiomeTags.IS_NEVER_WARM)
                .add(BiomeKeys.STONY_PEAKS)
                .addOptionalTag(ConventionalBiomeTags.IS_BEACH);
        // TODO: is_scorching

        getOrCreateTagBuilder(SClimateBiomeTags.IS_RAINY_OVERWORLD_CLIMATE)
                .addOptionalTag(Scorchful.id("humid_biomes"))
                .addOptionalTag(ConventionalBiomeTags.IS_WET_OVERWORLD)
                .addOptionalTag(ConventionalBiomeTags.IS_SWAMP)
                .addOptionalTag(BiomeTags.IS_JUNGLE);

        getOrCreateTagBuilder(SClimateBiomeTags.IS_NOT_RAINY_OVERWORLD_CLIMATE);
        // TODO: is_arid
    }
}