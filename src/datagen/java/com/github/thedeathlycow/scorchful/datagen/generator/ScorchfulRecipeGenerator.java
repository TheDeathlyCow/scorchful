package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.entity.v1.predicate.TemperatureLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ScorchfulRecipeGenerator extends FabricRecipeProvider {
    public ScorchfulRecipeGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.FOOD, SItems.CACTUS_JUICE)
                        .unlockedBy(getHasName(Items.CACTUS), has(Items.CACTUS))
                        .requires(Items.GLASS_BOTTLE)
                        .requires(Items.CACTUS)
                        .requires(Items.CACTUS)
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, Items.SAND, 1)
                        .unlockedBy(getHasName(SItems.DUST), has(SItems.DUST))
                        .pattern("##")
                        .pattern("##")
                        .define('#', SItems.DUST)
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, Items.RED_SAND, 1)
                        .unlockedBy(getHasName(SItems.RED_DUST), has(SItems.RED_DUST))
                        .pattern("##")
                        .pattern("##")
                        .define('#', SItems.RED_DUST)
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(
                                this.tag(SItemTags.SAND_DUSTS),
                                RecipeCategory.MISC,
                                CookingBookCategory.BLOCKS,
                                Items.QUARTZ,
                                0.1F, 200
                        )
                        .unlockedBy(getHasName(SItems.DUST), this.has(SItemTags.SAND_DUSTS))
                        .save(this.output, "quartz_from_dust");

                shaped(RecipeCategory.BUILDING_BLOCKS, SItems.RED_SAND_PILE, 6)
                        .unlockedBy(getHasName(Items.RED_SAND), has(Items.RED_SAND))
                        .pattern("###")
                        .define('#', Items.RED_SAND)
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, SItems.SAND_PILE, 6)
                        .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                        .pattern("###")
                        .define('#', Items.SAND)
                        .save(output);

                shaped(RecipeCategory.COMBAT, SItems.SUN_HAT)
                        .unlockedBy("is_player_warm", createWarmPlayerCondition())
                        .pattern("###")
                        .pattern("# #")
                        .define('#', Items.WHEAT)
                        .save(output);

                shaped(RecipeCategory.FOOD, SItems.WATER_SKIN)
                        .unlockedBy("in_water", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.WATER))
                        .pattern(" #I")
                        .pattern("# #")
                        .pattern(" # ")
                        .define('#', Items.LEATHER)
                        .define('I', Items.IRON_INGOT)
                        .save(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_CHESTPLATE)
                        .unlockedBy(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .define('#', Items.TURTLE_SCUTE)
                        .save(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_LEGGINGS)
                        .unlockedBy(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', Items.TURTLE_SCUTE)
                        .save(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_BOOTS)
                        .unlockedBy(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', Items.TURTLE_SCUTE)
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "ScorchfulRecipeGenerator";
    }


    private static Criterion<PlayerTrigger.TriggerInstance> createWarmPlayerCondition() {
        LootItemCondition condition = TemperatureLootCondition.builder(MinMaxBounds.Doubles.atLeast(0.25)).build();
        return CriteriaTriggers.LOCATION.createCriterion(
                new PlayerTrigger.TriggerInstance(Optional.of(ContextAwarePredicate.create(condition)))
        );
    }
}