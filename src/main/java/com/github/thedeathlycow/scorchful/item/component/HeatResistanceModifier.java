package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class HeatResistanceModifier {
    private static final Map<EquipmentSlot, Identifier> SLOT_IDS = new EnumMap<>(EquipmentSlot.class);
    private static final Map<EquipmentSlot, Identifier> ENVIRONMENT_SLOT_IDS = new EnumMap<>(EquipmentSlot.class);

    public static void initialize() {
        modifyVanillaItemComponents();
        initializeItemModifiers();
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
                    if (stack.contains(DataComponentTypes.EQUIPPABLE)) {
                        HeatResistanceComponent resistance = stack.getOrDefault(
                                SDataComponentTypes.HEAT_RESISTANCE,
                                HeatResistanceComponent.DEFAULT
                        );

                        EquipmentSlot slot = stack.get(DataComponentTypes.EQUIPPABLE).slot();
                        AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(slot);

                        if (resistance.heatResistance() != 0) {
                            builder.add(
                                    ThermooAttributes.HEAT_RESISTANCE,
                                    new EntityAttributeModifier(
                                            SLOT_IDS.computeIfAbsent(
                                                    slot,
                                                    sl -> Scorchful.id("base_heat_resistance/" + sl.asString())
                                            ),
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