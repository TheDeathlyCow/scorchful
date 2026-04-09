package com.github.thedeathlycow.scorchful.datagen.generator;


import com.github.thedeathlycow.scorchful.ScorchfulModMenu;
import com.github.thedeathlycow.scorchful.client.config.section.AccessibilitySettings;
import com.github.thedeathlycow.scorchful.client.config.section.DisplaySettings;
import com.github.thedeathlycow.scorchful.config.Translate;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class EnglishUSGenerator extends FabricLanguageProvider {
    public EnglishUSGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {
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

        builder.add(SItems.DUST, "Dust");
        builder.add(SItems.RED_DUST, "Red Dust");

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
        builder.add(SItemTags.COMMON_SAND_DUSTS, "Sand Dusts");

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

        builder.add(statusEffect(SMobEffects.HEAT_STROKE), "Heat Stroke");
        builder.add(statusEffect(SMobEffects.FEAR), "Fear");

        addDamageType(builder, SDamageTypes.HEAT, "%1$s couldn't handle the heat", "%1$s couldn't handle the heat of %2$s");
        addDamageType(builder, SDamageTypes.SUFFOCATE, "%1$s suffocated", "%1$s was suffocated by %2$s");

        addAdvancement(builder, SAdvancements.DRINK_CACTUS_JUICE, "It's the Quenchiest!", "Drink a Bottle of Cactus Juice");
        addAdvancement(builder, SAdvancements.OBTAIN_TURTLE_ARMOR, "Duck and Cover", "Obtain a piece of Turtle Armor");
        addAdvancement(builder, SAdvancements.SHOOT_PARANOIA_ARROW, "The Mind Killer", "Be struck with Fear");

        builder.add(SSoundEvents.ITEM_WATER_SKIN_FILL, "Waterskin fills");
        builder.add(SSoundEvents.TEMPERATURE_EFFECT_HEARTBEAT, "Heart beats");
        builder.add(SSoundEvents.REHYDRATE, "Player Rehydrates");
        builder.add(SSoundEvents.CRIMSON_LILY_SQUELCH, "Crimson Lily Squelches");
        builder.add(SSoundEvents.WEATHER_SANDSTORM, "Wind blows");
        builder.add(SSoundEvents.ENTITY_GULP, "Player gulps");
        builder.add(SSoundEvents.DISCOVER_VISION, "Player discovers vision");
        builder.add(SSoundEvents.TEMPERATURE_EFFECT_PANT, "Dog pants");

        addStat(builder, SStats.FILL_CRIMSON_LILY, "Filled Crimson Lily");
        addStat(builder, SStats.SOAKED_BY_CRIMSON_LILY, "Soaked by Crimson Lily");
        addStat(builder, SStats.USE_WARPED_LILY, "Harvest Warped Lily");

        // Config values
        builder.add(ScorchfulModMenu.TITLE, "Scorchful");
        builder.add(ScorchfulModMenu.CLIENT_TITLE, "Client Settings");
        builder.add(ScorchfulModMenu.COMMON_TITLE, "Common Settings");
        builder.add(ScorchfulModMenu.COMPAT_TITLE, "Compatibility Settings");

        builder.add(ScorchfulModMenu.ACCESSIBILITY_CATEGORY, "Accessibility Settings");
        builder.add(ScorchfulModMenu.ACCESSIBILITY_DESC, "Client-side accessibility settings for Scorchful.");

        builder.add(ScorchfulModMenu.DISPLAY_CATEGORY, "Display Settings");
        builder.add(ScorchfulModMenu.DISPLAY_DESC, "Client-side display settings for Scorchful.");

        builder.add(ScorchfulModMenu.TEMPERATURE_CATEGORY, "Temperature Settings");
        builder.add(ScorchfulModMenu.TEMPERATURE_DESC, "Settings for the Scorchful temperature system.");
        builder.add(Translate.categoryKey(TemperatureConfig.HANDLER, TemperatureConfig.GENERAL_CATEGORY_NAME), "General Settings");
        builder.add(Translate.categoryKey(TemperatureConfig.HANDLER, TemperatureConfig.ENVIRONMENT_CATEGORY_NAME), "Environment Settings");
        builder.add(Translate.categoryKey(TemperatureConfig.HANDLER, TemperatureConfig.TEMPERATURE_SOURCES_CATEGORY_NAME), "Temperature Sources");

        builder.add(ScorchfulModMenu.ENTITY_CATEGORY, "Entity Settings");
        builder.add(ScorchfulModMenu.ENTITY_DESC, "Specific settings for entities and mobs.");
        builder.add(Translate.categoryKey(EntityConfig.HANDLER, EntityConfig.SOAKING_CATEGORY), "Soaking Settings");
        builder.add(Translate.mainGroupKey(EntityConfig.HANDLER, EntityConfig.EFFECTS_GROUP), "Mob Effects");

        builder.add(ScorchfulModMenu.ITEM_CATEGORY, "Item Settings");
        builder.add(ScorchfulModMenu.ITEM_DESC, "Specific settings for items.");
        builder.add(Translate.categoryKey(ItemConfig.HANDLER, ItemConfig.TOOLS_AND_ARMOR_CATEGORY_NAME), "Tool & Armor Settings");
        builder.add(Translate.categoryKey(ItemConfig.HANDLER, ItemConfig.CONSUMABLE_CATEGORY_NAME), "Consumable Settings");
        builder.add(Translate.categoryKey(ItemConfig.HANDLER, ItemConfig.MISC_CATEGORY_NAME), "Miscellaneous Settings");

        builder.add(ScorchfulModMenu.WEATHER_CATEGORY, "Weather Settings");
        builder.add(ScorchfulModMenu.WEATHER_DESC, "Specific settings for Scorchful's weather-related effects.");
        builder.add(Translate.mainGroupKey(WeatherConfig.HANDLER, WeatherConfig.SUFFOCATING_GROUP), "Suffocating Sandstorms");

        builder.add(ScorchfulModMenu.DEHYDRATION_CATEGORY, "Dehydration Compatibility Settings");
        builder.add(ScorchfulModMenu.DEHYDRATION_DESC, "Specific settings for Scorchful's builtin compatibility with the Dehydration mod. This is only relevant if you use Dehydration.");

        generateConfigOptionTranslations(AccessibilitySettings.HANDLER, builder);
        generateConfigOptionTranslations(DisplaySettings.HANDLER, builder);
        generateConfigOptionTranslations(TemperatureConfig.HANDLER, builder);
        generateConfigOptionTranslations(EntityConfig.HANDLER, builder);
        generateConfigOptionTranslations(ItemConfig.HANDLER, builder);
        generateConfigOptionTranslations(WeatherConfig.HANDLER, builder);
        generateConfigOptionTranslations(DehydrationConfig.HANDLER, builder);

        generateConfigEnumTranslations(builder, FireChargeThrower.FireballFactory.class, "Disabled", "Small", "Large");
    }

    private String itemSuffix(Item item, String suffix) {
        return item.getDescriptionId() + "." + suffix;
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

    private String potionItem(Item item, Holder<Potion> potion) {
        return item.getDescriptionId() + ".effect." + potion.value().name();
    }

    private String enchantmentDesc(ResourceKey<Enchantment> key) {
        return Util.makeDescriptionId("enchantment", key.identifier()) + ".desc";
    }

    private String statusEffect(Holder<MobEffect> effect) {
        return Util.makeDescriptionId("effect", effect.unwrapKey().orElseThrow().identifier());
    }

    private void addDamageType(
            TranslationBuilder builder,
            ResourceKey<DamageType> key,
            String deathMessage,
            String playerDeathMessage
    ) {
        String translationKey = Util.makeDescriptionId("death.attack", key.identifier());

        builder.add(translationKey, deathMessage);
        builder.add(translationKey + ".player", playerDeathMessage);
    }

    private void addAdvancement(
            TranslationBuilder builder,
            ResourceKey<Advancement> key,
            String title,
            String desc
    ) {
        String translationKey = Util.makeDescriptionId("advancements", key.identifier());

        builder.add(translationKey + ".title", title);
        builder.add(translationKey + ".desc", desc);
    }

    private void addStat(
            TranslationBuilder builder,
            Identifier stat,
            String name
    ) {
        builder.add(Util.makeDescriptionId("stat", stat), name);
    }

    private <T> void generateConfigOptionTranslations(
            ConfigClassHandler<T> handler,
            TranslationBuilder builder
    ) {
        final String prefix = Translate.prefixKey(handler);

        for (Field field : handler.configClass().getDeclaredFields()) {
            SerialEntry entry = field.getAnnotation(SerialEntry.class);
            if (entry == null) {
                continue;
            }

            Translate.Name nameData = field.getAnnotation(Translate.Name.class);
            String nameKey = configOption(prefix, field.getName());

            if (nameData != null) {
                builder.add(nameKey, nameData.value());
            } else {
                throw new IllegalStateException("Option name missing for" + nameKey);
            }

            String comment = entry.comment();
            String commentKey = commentKey(prefix, field.getName());

            if (comment != null && !comment.isEmpty()) {
                builder.add(commentKey, comment);
            } else if (field.getAnnotation(Translate.NoComment.class) == null) {
                throw new IllegalStateException("Missing comment or @NoComment marker for " + commentKey);
            }
        }
    }

    private <E extends Enum<E> & StringRepresentable> void generateConfigEnumTranslations(
            TranslationBuilder builder,
            Class<E> enumClass,
            String... names
    ) {
        E[] entries = enumClass.getEnumConstants();
        if (entries.length != names.length) {
            throw new IllegalStateException(
                    "Names array length %d is different from enums array length %d"
                            .formatted(names.length, entries.length)
            );
        }

        for (E entry : enumClass.getEnumConstants()) {
            String key = "yacl3.config.enum.%s.%s".formatted(enumClass.getSimpleName(), entry.getSerializedName());
            builder.add(key, names[entry.ordinal()]);
        }
    }

    private static String configOption(String prefix, String name) {
        return prefix + "." + name;
    }

    private static String commentKey(String prefix, String name) {
        return configOption(prefix, name) + ".desc";
    }
}