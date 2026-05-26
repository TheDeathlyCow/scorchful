package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.datagen.ScorchfulDataGenerator;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(SItemTags.HEAT_RESISTANCE_MODIFIED)
                .addOptionalTag(ConventionalItemTags.ARMORS);

        tag(SItemTags.VERY_PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/turtle"));

        tag(SItemTags.PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/netherite"));

        tag(SItemTags.NEUTRAL_HEAT_RESISTANCE)
                .addOptionalTag(ResourceLocation.parse("thermoo-patches-stellaris-patch:space_suits"))
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/golden"))
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/chainmail"));

        tag(SItemTags.VERY_HARMFUL_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/fur"));

        tag(SItemTags.COMMON_CACTUS_JUICE)
                .add(SItems.CACTUS_JUICE);

        tag(ConventionalItemTags.JUICE_DRINKS)
                .addOptionalTag(SItemTags.COMMON_CACTUS_JUICE);

        tag(ConventionalItemTags.WATER_DRINKS)
                .add(SItems.WATER_SKIN);

        tag(ConventionalItemTags.DRINK_CONTAINING_BOTTLE)
                .add(SItems.CACTUS_JUICE);
    }
}