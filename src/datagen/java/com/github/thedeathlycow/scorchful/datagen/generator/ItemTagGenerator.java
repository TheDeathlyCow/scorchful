package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.datagen.ScorchfulDataGenerator;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SItemTags.HEAT_RESISTANCE_MODIFIED)
                .addOptionalTag(ConventionalItemTags.ARMORS);

        getOrCreateTagBuilder(SItemTags.VERY_PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/turtle"));

        getOrCreateTagBuilder(SItemTags.PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/netherite"));

        getOrCreateTagBuilder(SItemTags.NEUTRAL_HEAT_RESISTANCE)
                .addOptionalTag(Identifier.of("thermoo-patches-stellaris-patch:space_suits"))
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/golden"))
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/chainmail"));

        getOrCreateTagBuilder(SItemTags.VERY_HARMFUL_HEAT_RESISTANCE)
                .addOptionalTag(ScorchfulDataGenerator.commonID("armor/fur"));

        getOrCreateTagBuilder(SItemTags.COMMON_CACTUS_JUICE)
                .add(SItems.CACTUS_JUICE);

        getOrCreateTagBuilder(ConventionalItemTags.JUICE_DRINKS)
                .addOptionalTag(SItemTags.COMMON_CACTUS_JUICE);

        getOrCreateTagBuilder(ConventionalItemTags.WATER_DRINKS)
                .add(SItems.WATER_SKIN);

        getOrCreateTagBuilder(ConventionalItemTags.DRINK_CONTAINING_BOTTLE)
                .add(SItems.CACTUS_JUICE);
    }
}