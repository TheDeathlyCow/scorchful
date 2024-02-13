package com.github.thedeathlycow.scorchful.worldgen;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class NetherBiomeModifications {

    public static void placeFeaturesInBiomes() {

        BiomeModification modification = BiomeModifications.create(Scorchful.id("nether_features"));

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(SBiomeTags.HAS_FEATURE_SPARSE_CRIMSON_LILY_PATCH),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Feature.VEGETAL_DECORATION,
                            placedFeatureRegistryKey("sparse_crimson_lily_patch")
                    );
                }
        );

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(SBiomeTags.HAS_FEATURE_CRIMSON_LILY_PATCH),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Feature.VEGETAL_DECORATION,
                            placedFeatureRegistryKey("crimson_lily_patch")
                    );
                }
        );
    }

    private static RegistryKey<PlacedFeature> placedFeatureRegistryKey(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Scorchful.id(id));
    }

    private NetherBiomeModifications() {

    }
}
