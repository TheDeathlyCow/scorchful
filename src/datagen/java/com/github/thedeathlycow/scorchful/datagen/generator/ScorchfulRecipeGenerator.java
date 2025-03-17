package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.thermoo.api.predicate.TemperatureLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ScorchfulRecipeGenerator extends FabricRecipeProvider {
    public ScorchfulRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShapeless(RecipeCategory.FOOD, SItems.CACTUS_JUICE)
                        .criterion(hasItem(Items.CACTUS), conditionsFromItem(Items.CACTUS))
                        .input(Items.GLASS_BOTTLE)
                        .input(Items.CACTUS)
                        .input(Items.CACTUS)
                        .offerTo(exporter);

                createShaped(RecipeCategory.BUILDING_BLOCKS, SItems.RED_SAND_PILE, 6)
                        .criterion(hasItem(Items.RED_SAND), conditionsFromItem(Items.RED_SAND))
                        .pattern("###")
                        .input('#', Items.RED_SAND)
                        .offerTo(exporter);

                createShaped(RecipeCategory.BUILDING_BLOCKS, SItems.SAND_PILE, 6)
                        .criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                        .pattern("###")
                        .input('#', Items.SAND)
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, SItems.SUN_HAT)
                        .criterion("is_player_warm", createWarmPlayerCondition())
                        .pattern("###")
                        .pattern("# #")
                        .input('#', Items.WHEAT)
                        .offerTo(exporter);

                createShaped(RecipeCategory.FOOD, SItems.WATER_SKIN)
                        .criterion("in_water", EnterBlockCriterion.Conditions.block(Blocks.WATER))
                        .pattern(" #I")
                        .pattern("# #")
                        .pattern(" # ")
                        .input('#', Items.LEATHER)
                        .input('I', Items.IRON_INGOT)
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, SItems.TURTLE_CHESTPLATE)
                        .criterion(hasItem(Items.TURTLE_SCUTE), conditionsFromItem(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, SItems.TURTLE_LEGGINGS)
                        .criterion(hasItem(Items.TURTLE_SCUTE), conditionsFromItem(Items.TURTLE_SCUTE))
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, SItems.TURTLE_BOOTS)
                        .criterion(hasItem(Items.TURTLE_SCUTE), conditionsFromItem(Items.TURTLE_SCUTE))
                        .pattern("# #")
                        .pattern("# #")
                        .input('#', Items.TURTLE_SCUTE)
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "ScorchfulRecipeGenerator";
    }


    private static AdvancementCriterion<TickCriterion.Conditions> createWarmPlayerCondition() {
        LootCondition condition = TemperatureLootCondition
                .builder(NumberRange.DoubleRange.atLeast(0.25))
                .build();
        return Criteria.LOCATION.create(
                new TickCriterion.Conditions(Optional.of(LootContextPredicate.create(condition)))
        );
    }
}