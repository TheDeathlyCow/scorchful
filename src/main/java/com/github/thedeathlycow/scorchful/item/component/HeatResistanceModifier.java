package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Equipment;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import javax.xml.crypto.Data;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class HeatResistanceModifier {
    private static final Map<EquipmentSlot, Identifier> SLOT_IDS = new EnumMap<>(EquipmentSlot.class);
    private static final Map<EquipmentSlot, Identifier> ENVIRONMENT_SLOT_IDS = new EnumMap<>(EquipmentSlot.class);

    public static void initialize() {
        initializeLegacyMaterialTags();
        modifyVanillaItemComponents();
        initializeItemModifiers();
    }

    private static void initializeLegacyMaterialTags() {
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(stack -> {
            if (!stack.contains(SDataComponentTypes.HEAT_RESISTANCE) && stack.getItem() instanceof ArmorItem armor) {
                RegistryEntry<ArmorMaterial> material = armor.getMaterial();
                if (material.isIn(ArmorMaterialTags.VERY_RESISTANT_TO_HEAT)) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE);
                } else if (material.isIn(ArmorMaterialTags.RESISTANT_TO_HEAT)) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.PROTECTIVE);
                } else if (material.isIn(ArmorMaterialTags.VERY_WEAK_TO_HEAT)) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_HARMFUL);
                } else if (material.isIn(SArmorMaterialTags.HEAT_NEUTRAL)) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.NEUTRAL);
                } else if (material.isIn(ArmorMaterialTags.WEAK_TO_HEAT)) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.DEFAULT);
                }
            }
        });
    }

    private static void modifyVanillaItemComponents() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.WOLF_ARMOR,
                    builder -> {
                        AttributeModifiersComponent attributes = builder.getOrDefault(
                                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                                AttributeModifiersComponent.DEFAULT
                        );

                        if (attributes.modifiers().isEmpty()) {
                            attributes = Items.WOLF_ARMOR.getAttributeModifiers();
                        }

                        attributes = attributes.with(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        Scorchful.id("base_heat_resistance"),
                                        8.0,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.BODY
                        );

                        builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes);
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.NEUTRAL);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    List.of(
                            Items.GOLDEN_HELMET,
                            Items.GOLDEN_CHESTPLATE,
                            Items.GOLDEN_LEGGINGS,
                            Items.GOLDEN_BOOTS,
                            Items.CHAINMAIL_HELMET,
                            Items.CHAINMAIL_CHESTPLATE,
                            Items.CHAINMAIL_LEGGINGS,
                            Items.CHAINMAIL_BOOTS
                    ),
                    (builder, item) -> {
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.NEUTRAL);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    List.of(
                            Items.NETHERITE_HELMET,
                            Items.NETHERITE_CHESTPLATE,
                            Items.NETHERITE_LEGGINGS,
                            Items.NETHERITE_BOOTS
                    ),
                    (builder, item) -> {
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.PROTECTIVE);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.TURTLE_HELMET,
                    builder -> {
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, HeatResistanceComponent.VERY_PROTECTIVE);
                    }
            );
        });
    }

    private static void initializeItemModifiers() {
        ModifyItemAttributeModifiersCallback.EVENT.register(
                (stack, builder) -> {
                    if (stack.getItem() instanceof ArmorItem armor) {
                        HeatResistanceComponent resistance = HeatResistanceComponent.get(stack);

                        EquipmentSlot slot = armor.getSlotType();
                        AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(slot);

                        if (resistance.heatResistance() != 0) {
                            builder.add(
                                    ThermooAttributes.HEAT_RESISTANCE,
                                    new EntityAttributeModifier(
                                            SLOT_IDS.computeIfAbsent(slot, sl -> Scorchful.id("base_heat_resistance/" + sl.asString())),
                                            resistance.heatResistance(),
                                            EntityAttributeModifier.Operation.ADD_VALUE
                                    ),
                                    modifierSlot
                            );
                        }

                        if (resistance.environmentHeatResistance() != 0) {
                            builder.add(
                                    ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                                    new EntityAttributeModifier(
                                            ENVIRONMENT_SLOT_IDS.computeIfAbsent(slot, sl -> Scorchful.id("base_environment_heat_resistance/" + sl.asString())),
                                            resistance.environmentHeatResistance(),
                                            EntityAttributeModifier.Operation.ADD_VALUE
                                    ),
                                    modifierSlot
                            );
                        }
                    }
                }
        );
    }

    private HeatResistanceModifier() {

    }
}