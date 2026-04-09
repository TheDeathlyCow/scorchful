package com.github.thedeathlycow.scorchful.datagen.generator.tag;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricTagsProvider.BlockTagsProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addScorchfulTags();
        addCommonTags();
    }

    private void addScorchfulTags() {
        valueLookupBuilder(SItemTags.HEAT_RESISTANCE_MODIFIED)
                .addOptionalTag(ConventionalItemTags.ARMORS);

        valueLookupBuilder(SItemTags.VERY_PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(commonKey("armor/turtle"));

        valueLookupBuilder(SItemTags.PROTECTIVE_HEAT_RESISTANCE)
                .addOptionalTag(commonKey("armor/netherite"));

        valueLookupBuilder(SItemTags.NEUTRAL_HEAT_RESISTANCE)
                .addOptionalTag(key("thermoo-patches-stellaris-patch", "space_suits"))
                .addOptionalTag(commonKey("armor/golden"))
                .addOptionalTag(commonKey("armor/chainmail"));

        valueLookupBuilder(SItemTags.VERY_HARMFUL_HEAT_RESISTANCE)
                .addOptionalTag(commonKey("armor/fur"));

        valueLookupBuilder(SItemTags.COMMON_CACTUS_JUICE)
                .add(SItems.CACTUS_JUICE);

    }

    private void addCommonTags() {
        valueLookupBuilder(ConventionalItemTags.JUICE_DRINKS)
                .addOptionalTag(SItemTags.COMMON_CACTUS_JUICE);

        valueLookupBuilder(ConventionalItemTags.WATER_DRINKS)
                .add(SItems.WATER_SKIN);

        valueLookupBuilder(ConventionalItemTags.DRINK_CONTAINING_BOTTLE)
                .add(SItems.CACTUS_JUICE);
    }

    private static TagKey<Item> commonKey(String path) {
        return key("c", path);
    }

    private static TagKey<Item> key(String id, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(id, path));
    }
}