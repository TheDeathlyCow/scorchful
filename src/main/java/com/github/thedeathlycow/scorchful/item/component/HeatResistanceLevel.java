package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public enum HeatResistanceLevel {
    VERY_PROTECTIVE(ArmorMaterialTags.VERY_RESISTANT_TO_HEAT, SItemTags.IS_VERY_PROTECTIVE_HEAT_RESISTANCE, CombatConfig::getVeryProtectiveArmorHeatResistanceMultiplier),
    PROTECTIVE(ArmorMaterialTags.RESISTANT_TO_HEAT, SItemTags.IS_PROTECTIVE_HEAT_RESISTANCE, CombatConfig::getProtectiveArmorHeatResistanceMultiplier),
    NEUTRAL(SArmorMaterialTags.HEAT_NEUTRAL, SItemTags.IS_NEUTRAL_HEAT_RESISTANCE, c -> 0),
    HARMFUL(s -> true, CombatConfig::getDefaultArmorHeatResistanceMultiplier),
    VERY_HARMFUL(ArmorMaterialTags.VERY_WEAK_TO_HEAT, SItemTags.IS_VERY_WEAK_HEAT_RESISTANCE, CombatConfig::getVeryHarmfulArmorHeatResistanceMultiplier);

    private final Predicate<ItemStack> appliesTo;

    private final ToDoubleFunction<CombatConfig> multiplier;

    HeatResistanceLevel(TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
        this(createTagPredicate(armorMaterialTag, itemTag), heatResistanceProvider);
    }

    HeatResistanceLevel(Predicate<ItemStack> appliesTo, ToDoubleFunction<CombatConfig> multiplier) {
        this.appliesTo = appliesTo;
        this.multiplier = multiplier;
    }

    public static double getMultiplierForStack(ItemStack stack, CombatConfig config) {
        for (HeatResistanceLevel level : values()) {
            if (level != HARMFUL && level.appliesTo.test(stack)) {
                return level.multiplier.applyAsDouble(config);
            }
        }
        return HARMFUL.multiplier.applyAsDouble(config);
    }

    private static Predicate<ItemStack> createTagPredicate(TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag) {
        return stack -> stack.isIn(itemTag)
                || (stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().isIn(armorMaterialTag));
    }
}