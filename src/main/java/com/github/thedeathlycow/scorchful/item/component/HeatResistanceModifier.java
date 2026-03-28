package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.item.v2.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;

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
                        ItemAttributeModifiers attributes = builder.getOrDefault(
                                DataComponents.ATTRIBUTE_MODIFIERS,
                                ItemAttributeModifiers.EMPTY
                        );

                        attributes = attributes.withModifierAdded(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new AttributeModifier(
                                        Scorchful.id("base_heat_resistance"),
                                        8.0,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.BODY
                        );

                        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
                        builder.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.NEUTRAL);
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
                        builder.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.NEUTRAL);
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
                        builder.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.PROTECTIVE);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.TURTLE_HELMET,
                    builder -> {
                        builder.set(SDataComponentTypes.HEAT_RESISTANCE, HeatResistance.VERY_PROTECTIVE);
                    }
            );
        });
    }

    private static void initializeItemModifiers() {
        ModifyItemAttributeModifiersCallback.EVENT.register(
                (stack, builder) -> {
                    if (stack.is(ConventionalItemTags.ARMORS) && stack.has(DataComponents.EQUIPPABLE)) {
                        HeatResistance resistance = HeatResistance.get(stack);

                        EquipmentSlot slot = stack.get(DataComponents.EQUIPPABLE).slot();
                        EquipmentSlotGroup modifierSlot = EquipmentSlotGroup.bySlot(slot);

                        if (resistance.heatResistance() != 0) {
                            builder.add(
                                    ThermooAttributes.HEAT_RESISTANCE,
                                    new AttributeModifier(
                                            SLOT_IDS.computeIfAbsent(
                                                    slot,
                                                    sl -> Scorchful.id("base_heat_resistance/" + sl.getSerializedName())
                                            ),
                                            resistance.heatResistance(),
                                            AttributeModifier.Operation.ADD_VALUE
                                    ),
                                    modifierSlot
                            );
                        }

                        if (resistance.environmentHeatResistance() != 0) {
                            builder.add(
                                    ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                                    new AttributeModifier(
                                            ENVIRONMENT_SLOT_IDS.computeIfAbsent(slot, sl -> Scorchful.id("base_environment_heat_resistance/" + sl.getSerializedName())),
                                            resistance.environmentHeatResistance(),
                                            AttributeModifier.Operation.ADD_VALUE
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