package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class EnglishUSGenerator extends FabricLanguageProvider {
    public EnglishUSGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
        builder.add("scorchful.title", "Scorchful");

        builder.add(SItems.WATER_SKIN, "Waterskin");
        builder.add(waterSkinSuffix("empty"), "Empty Waterskin");
        builder.add(waterSkinSuffix("partially_filled"), "Partially Filled Waterskin");
        builder.add(waterSkinSuffix("filled"), "Filled Waterskin");
        builder.add(waterSkinSuffix("tooltip.count"), "Drinks Remaining %1$s / %2$s");
        builder.add(waterSkinSuffix("tooltip.empty"), "Empty");

        builder.add(SItems.SUN_HAT, "Sun Hat");
        builder.add(tooltip(SItems.SUN_HAT), "Reduces heat from the Sun when worn");

        builder.add(SItems.CACTUS_JUICE, "Bottle of Cactus Juice");

        builder.add(SItems.TURTLE_CHESTPLATE, "Turtle Carapace");
        builder.add(SItems.TURTLE_LEGGINGS, "Turtle Knee Pads");
        builder.add(SItems.TURTLE_BOOTS, "Turtle Flippers");

        builder.add(genericTooltip("cooling"), "Cooling ❄");
        builder.add(genericTooltip("refreshing"), "Refreshing \uD83C\uDF0A");
        builder.add(genericTooltip("sustaining"), "Sustaining \uD83C\uDF0A\uD83C\uDF0A");
        builder.add(genericTooltip("hydrating"), "Hydrating \uD83C\uDF0A\uD83C\uDF0A\uD83C\uDF0A");
        builder.add(genericTooltip("parching"), "Parching ☀");

        builder.add(potionItem(Items.POTION, SPotions.PARANOIA), "Potion of Paranoia");
        builder.add(potionItem(Items.SPLASH_POTION, SPotions.PARANOIA), "Splash Potion of Paranoia");
        builder.add(potionItem(Items.LINGERING_POTION, SPotions.PARANOIA), "Lingering Potion of Paranoia");
        builder.add(potionItem(Items.TIPPED_ARROW, SPotions.PARANOIA), "Arrow of Paranoia");

        builder.add(SItemTags.IS_COOLING_FOOD, "Cooling food");
        builder.add(SItemTags.IS_HYDRATING, "Hydrating Food and Drink");
        builder.add(SItemTags.IS_PARCHING, "Parching Food and Drink");
        builder.add(SItemTags.IS_REFRESHING, "Refreshing Food and Drink");
        builder.add(SItemTags.IS_SUSTAINING, "Sustaining Food and Drink");
        builder.add(SItemTags.SAND_PILES, "Sand Piles");
        builder.add(SItemTags.TURTLE_ARMOR, "Turtle Armor");
        builder.add(SItemTags.BLOCKS_RAIN_WHEN_HOLDING, "Rain blocker");
        builder.add(SItemTags.VERY_HARMFUL_HEAT_RESISTANCE, "Very Harmful Heat Resistance");
        builder.add(SItemTags.NEUTRAL_HEAT_RESISTANCE, "Neutral Heat Resistance");
        builder.add(SItemTags.PROTECTIVE_HEAT_RESISTANCE, "Protective Heat Resistance");
        builder.add(SItemTags.VERY_PROTECTIVE_HEAT_RESISTANCE, "Very Protective Heat Resistance");
        builder.add(SItemTags.COMMON_CACTUS_JUICE, "Cactus Juice");

        builder.add(SEntityAttributes.REHYDRATION_EFFICIENCY, "Rehydration Efficiency");
        builder.add(SEntityAttributes.LUNG_CAPACITY, "Lung Capacity");

        builder.add(SBlocks.CRIMSON_LILY, "Crimson Lily");
        builder.add(SBlocks.WARPED_LILY, "Warped Lily");
        builder.add(SBlocks.ROOTED_NETHERRACK, "Rooted Netherrack");
        builder.add(SBlocks.ROOTED_CRIMSON_NYLIUM, "Rooted Crimson Nylium");
        builder.add(SBlocks.ROOTED_WARPED_NYLIUM, "Rooted Warped Nylium");
        builder.add(SBlocks.SAND_PILE, "Sand Pile");
        builder.add(SBlocks.RED_SAND_PILE, "Red Sand Pile");
        builder.add(SBlocks.SAND_CAULDRON, "Sand Cauldron");
        builder.add(SBlocks.RED_SAND_CAULDRON, "Red Sand Cauldron");

        builder.addEnchantment(SEnchantmentKeys.REHYDRATION, "Rehydration");
        builder.add(enchantmentDesc(SEnchantmentKeys.REHYDRATION), "Replenishes body water lost from sweating");

        builder.add(statusEffect(SStatusEffects.HEAT_STROKE), "Heat Stroke");
        builder.add(statusEffect(SStatusEffects.FEAR), "Fear");
    }

    private String itemSuffix(Item item, String suffix) {
        return item.getTranslationKey() + "." + suffix;
    }

    private String tooltip(Item item) {
        return itemSuffix(item, "tooltip");
    }

    private String genericTooltip(String suffix) {
        return "item.scorchful.tooltip." + suffix;
    }

    private String waterSkinSuffix(String suffix) {
        return itemSuffix(SItems.WATER_SKIN, suffix);
    }

    private String potionItem(Item item, RegistryEntry<Potion> potion) {
        return item.getTranslationKey() + ".effect." + potion.value().getBaseName();
    }

    private String enchantmentDesc(RegistryKey<Enchantment> key) {
        return Util.createTranslationKey("enchantment.desc", key.getValue());
    }

    private String statusEffect(RegistryEntry<StatusEffect> effect) {
        return Util.createTranslationKey("effect", effect.getKey().orElseThrow().getValue());
    }
}