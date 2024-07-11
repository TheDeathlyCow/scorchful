package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.mixin.accessor.ComponentMapBuilderAccessor;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class HeatResistanceHelper {

    private static final Identifier MODIFIER_ID = Scorchful.id("base_heat_resistance");

    public static void modifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context) {
        context.modify(ArmorItem.class::isInstance, HeatResistanceHelper::build);
    }

    private static void build(ComponentMap.Builder builder, Item item) {
        if (item instanceof ArmorItem armorItem) {
            CombatConfig config = Scorchful.getConfig().combatConfig;
            ComponentMapBuilderAccessor accessor = (ComponentMapBuilderAccessor) builder;
            AttributeModifiersComponent attributes = (AttributeModifiersComponent) accessor.scorchful$components()
                    .getOrDefault(
                            DataComponentTypes.ATTRIBUTE_MODIFIERS,
                            AttributeModifiersComponent.DEFAULT
                    );

            HeatResistanceLevel level = HeatResistanceLevel.forItem(armorItem);

            double resistance = level.getHeatResistance(config);

            if (resistance != 0 && !Double.isNaN(resistance)) {

                AttributeModifiersComponent modifiers = attributes
                        .with(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        MODIFIER_ID,
                                        resistance,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.forEquipmentSlot(armorItem.getSlotType())
                        );

                builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, modifiers);
            }
        }
    }

    public enum HeatResistanceLevel {
        VERY_PROTECTIVE(SItemTags.HEAT_VERY_PROTECTIVE, CombatConfig::getVeryProtectiveArmorHeatResistance),
        PROTECTIVE(SItemTags.HEAT_PROTECTIVE, CombatConfig::getProtectiveArmorHeatResistance),
        VERY_HARMFUL(SItemTags.HEAT_VERY_HARMFUL, CombatConfig::getVeryHarmfulArmorHeatResistance),
        NEUTRAL(SItemTags.HEAT_NEUTRAL, c -> Double.NaN),
        DEFAULT(item -> true, CombatConfig::getDefaultArmorHeatResistance);


        private final Predicate<Item> predicate;

        private final ToDoubleFunction<CombatConfig> heatResistanceProvider;

        HeatResistanceLevel(TagKey<Item> tag, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this(item -> item.getRegistryEntry().isIn(tag), heatResistanceProvider);
        }

        HeatResistanceLevel(Predicate<Item> predicate, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
            this.predicate = predicate;
            this.heatResistanceProvider = heatResistanceProvider;
        }

        private double getHeatResistance(CombatConfig config) {
            return this.heatResistanceProvider.applyAsDouble(config);
        }

        public boolean appliesToItem(Item item) {
            return this.predicate.test(item);
        }

        public static HeatResistanceLevel forItem(Item item) {
            for (HeatResistanceLevel level : values()) {
                if (level.appliesToItem(item)) {
                    return level;
                }
            }
            return DEFAULT;
        }
    }
}
