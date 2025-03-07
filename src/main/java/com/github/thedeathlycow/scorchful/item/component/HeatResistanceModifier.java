package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Equipment;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import javax.xml.crypto.Data;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class HeatResistanceModifier {
    private static final Map<EquipmentSlot, Identifier> HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);
    private static final Map<EquipmentSlot, Identifier> ENVIRONMENT_HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);

    public static void initialize() {
        initializeLegacyMaterialTags();
        modifyVanillaItemComponents();
        initializeItemModifiers();
    }

    private static void initializeLegacyMaterialTags() {
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(stack -> {
            boolean shouldCheckMaterialTag = stack.isIn(ConventionalItemTags.ARMORS)
                    && !stack.contains(SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS);

            if (shouldCheckMaterialTag) {
                LegacyHeatResistanceLevel level = LegacyHeatResistanceLevel.forItem(stack.getItem());
                if (level != null) {
                    stack.set(SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS, level.getExtraAttributeModifiers());
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
                        builder.add(SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS, ExtraAttributeModifierComponent.EMPTY);
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
                        builder.add(
                                SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS,
                                LegacyHeatResistanceLevel.PROTECTIVE.getExtraAttributeModifiers()
                        );
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.TURTLE_HELMET,
                    builder -> {
                        builder.add(SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS, TurtleArmorEffects.EXTRA_ATTRIBUTES);
                    }
            );
        });
    }

    private static void initializeItemModifiers() {
        ModifyItemAttributeModifiersCallback.EVENT.register(
                (stack, builder) -> {
                    if (stack.getItem() instanceof Equipment equipment) {
                        EquipmentSlot slot = equipment.getSlotType();
                        AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(slot);

                        ExtraAttributeModifierComponent extraModifiers = stack.getOrDefault(
                                SDataComponentTypes.EXTRA_ATTRIBUTE_MODIFIERS,
                                stack.isIn(ConventionalItemTags.ARMORS)
                                        ? ExtraAttributeModifierComponent.DEFAULT
                                        : ExtraAttributeModifierComponent.EMPTY
                        );

                        extraModifiers.modifiers().forEach(modifier -> {
                            builder.add(
                                    modifier.attribute(),
                                    new EntityAttributeModifier(
                                            modifier.id().withSuffixedPath("/" + slot.asString()),
                                            modifier.amount(),
                                            modifier.operation()
                                    ),
                                    modifierSlot
                            );
                        });
                    }
                }
        );
    }

    private HeatResistanceModifier() {

    }
}