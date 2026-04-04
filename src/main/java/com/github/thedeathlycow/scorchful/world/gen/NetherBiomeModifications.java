package com.github.thedeathlycow.scorchful.world.gen;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class NetherBiomeModifications {
    public static void initialize() {
        BiomeModification modification = BiomeModifications.create(Scorchful.id("nether_features"));

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(SBiomeTags.HAS_FEATURE_SPARSE_CRIMSON_LILY_PATCH),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatureRegistryKey("sparse_crimson_lily_patch")
                    );
                }
        );

        modification.add(
                ModificationPhase.ADDITIONS,
                BiomeSelectors.tag(SBiomeTags.HAS_FEATURE_CRIMSON_LILY_PATCH),
                (biomeSelectionContext, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatureRegistryKey("crimson_lily_patch")
                    );
                }
        );
    }

    private static ResourceKey<PlacedFeature> placedFeatureRegistryKey(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Scorchful.id(id));
    }

    private NetherBiomeModifications() {

    }
}
