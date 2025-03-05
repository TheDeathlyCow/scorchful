package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.event.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class HeatResistanceHelper {
    public static void initialize() {
        ModifyItemAttributeModifiersCallback.EVENT.register((stack, builder) -> {
            if (stack.getItem() instanceof ArmorItem armorItem) {
                CombatConfig config = Scorchful.getConfig().combatConfig;
                HeatResistanceLevel level = HeatResistanceLevel.forStack(stack);
                builder.add(
                        ThermooAttributes.HEAT_RESISTANCE,
                        new EntityAttributeModifier(
                                Scorchful.id("base_heat_resistance"),
                                level.getHeatResistance(config),
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.forEquipmentSlot(armorItem.getSlotType())
                );
            }
        });
    }

    public enum HeatResistanceLevel {
        VERY_PROTECTIVE(ArmorMaterialTags.VERY_RESISTANT_TO_HEAT, SItemTags.IS_VERY_PROTECTIVE_HEAT_RESISTANCE, CombatConfig::getVeryProtectiveArmorHeatResistance),
        PROTECTIVE(ArmorMaterialTags.RESISTANT_TO_HEAT, SItemTags.IS_PROTECTIVE_HEAT_RESISTANCE, CombatConfig::getProtectiveArmorHeatResistance),
        VERY_HARMFUL(ArmorMaterialTags.VERY_WEAK_TO_HEAT, SItemTags.IS_VERY_WEAK_HEAT_RESISTANCE, CombatConfig::getVeryHarmfulArmorHeatResistance),
        NEUTRAL(SArmorMaterialTags.HEAT_NEUTRAL, SItemTags.IS_NEUTRAL_HEAT_RESISTANCE, c -> Double.NaN),
        DEFAULT(item -> true, CombatConfig::getDefaultArmorHeatResistance);

        private final Predicate<ItemStack> predicate;

        private final ToDoubleFunction<CombatConfig> heatResistanceProvider;

        HeatResistanceLevel(TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this(createTagPredicate(armorMaterialTag, itemTag), heatResistanceProvider);
        }

        HeatResistanceLevel(Predicate<ItemStack> predicate, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this.predicate = predicate;
            this.heatResistanceProvider = heatResistanceProvider;
        }

        public double getHeatResistance(CombatConfig config) {
            return this.heatResistanceProvider.applyAsDouble(config);
        }

        public static HeatResistanceLevel forStack(ItemStack stack) {
            for (HeatResistanceLevel level : values()) {
                if (level.predicate.test(stack)) {
                    return level;
                }
            }
            return DEFAULT;
        }

        private static Predicate<ItemStack> createTagPredicate(TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag) {
            return stack -> stack.isIn(itemTag)
                    || (stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().isIn(armorMaterialTag));
        }
    }
}
