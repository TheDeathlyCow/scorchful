package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.thermoo.api.predicate.TemperatureLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ScorchfulRecipeGenerator extends FabricRecipeProvider {
    public ScorchfulRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.FOOD, SItems.CACTUS_JUICE)
                        .criterion(getHasName(Items.CACTUS), has(Items.CACTUS))
                        .input(Items.GLASS_BOTTLE)
                        .input(Items.CACTUS)
                        .input(Items.CACTUS)
                        .offerTo(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, SItems.RED_SAND_PILE, 6)
                        .criterion(getHasName(Items.RED_SAND), has(Items.RED_SAND))
                        .pattern("###")
                        .input('#', Items.RED_SAND)
                        .offerTo(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, SItems.SAND_PILE, 6)
                        .criterion(getHasName(Items.SAND), has(Items.SAND))
                        .pattern("###")
                        .input('#', Items.SAND)
                        .offerTo(output);

                shaped(RecipeCategory.COMBAT, SItems.SUN_HAT)
                        .criterion("is_player_warm", createWarmPlayerCondition())
                        .pattern("###")
                        .pattern("# #")
                        .input('#', Items.WHEAT)
                        .offerTo(output);

                shaped(RecipeCategory.FOOD, SItems.WATER_SKIN)
                        .criterion("in_water", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.WATER))
                        .pattern(" #I")
                        .pattern("# #")
                        .pattern(" # ")
                        .input('#', Items.LEATHER)
                        .input('I', Items.IRON_INGOT)
                        .offerTo(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_CHESTPLATE)
                        .criterion(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_LEGGINGS)
                        .criterion(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(output);

                shaped(RecipeCategory.COMBAT, SItems.TURTLE_BOOTS)
                        .criterion(getHasName(Items.TURTLE_SCUTE), has(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("# #")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(output);
            }
        };
    }

    @Override
    public String getName() {
        return "ScorchfulRecipeGenerator";
    }


    private static Criterion<PlayerTrigger.TriggerInstance> createWarmPlayerCondition() {
        LootItemCondition condition = TemperatureLootCondition
                .builder(MinMaxBounds.Doubles.atLeast(0.25))
                .build();
        return CriteriaTriggers.LOCATION.createCriterion(
                new PlayerTrigger.TriggerInstance(Optional.of(ContextAwarePredicate.create(condition)))
        );
    }
}