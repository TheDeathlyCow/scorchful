package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Equipment;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class HeatResistanceModifier {
    private static final Map<EquipmentSlot, Identifier> HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);
    private static final Map<EquipmentSlot, Identifier> ENVIRONMENT_HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);

    public static void initialize() {
        initializeLegacyMaterialTags();
        modifyVanillaItemComponents();

        ModifyItemAttributeModifiersCallback.EVENT.register(
                (stack, builder) -> {
                    if (stack.getItem() instanceof Equipment equipment) {
                        EquipmentSlot slot = equipment.getSlotType();
                        AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(slot);

                        ExtraAttributeModifierComponent heatResistance = stack.getOrDefault(
                                SDataComponentTypes.HEAT_RESISTANCE,
                                ExtraAttributeModifierComponent.DEFAULT_HEAT_RESISTANCE
                        );
                        builder.add(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        HEAT_RESISTANCE_ID.computeIfAbsent(slot, s -> Scorchful.id("base_heat_resistance/" + s.asString())),
                                        heatResistance.amount(),
                                        heatResistance.operation()
                                ),
                                modifierSlot
                        );

                        ExtraAttributeModifierComponent environmentalHeatResistance = stack.getOrDefault(
                                SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE,
                                ExtraAttributeModifierComponent.DEFAULT_ENVIRONMENT_HEAT_RESISTANCE
                        );
                        builder.add(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        ENVIRONMENT_HEAT_RESISTANCE_ID.computeIfAbsent(slot, s -> Scorchful.id("base_environment_heat_resistance/" + s.asString())),
                                        environmentalHeatResistance.amount(),
                                        environmentalHeatResistance.operation()
                                ),
                                modifierSlot
                        );
                    }
                }
        );
    }

    private static void initializeLegacyMaterialTags() {
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(stack -> {
            boolean shouldCheckMaterialTag = stack.isIn(ConventionalItemTags.ARMORS)
                    && !stack.contains(SDataComponentTypes.HEAT_RESISTANCE)
                    && !stack.contains(SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE);

            if (shouldCheckMaterialTag) {
                LegacyHeatResistanceLevel level = LegacyHeatResistanceLevel.forItem(stack.getItem());
                if (level != null) {
                    stack.set(SDataComponentTypes.HEAT_RESISTANCE, level.getHeatResistance());
                    stack.set(SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE, level.getEnvironmentHeatResistance());
                }
            }
        });
    }

    private static void modifyVanillaItemComponents() {
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
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, ExtraAttributeModifierComponent.NO_RESISTANCE);
                        builder.add(SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE, ExtraAttributeModifierComponent.NO_RESISTANCE);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            ExtraAttributeModifierComponent netheriteHeatResistance = new ExtraAttributeModifierComponent(0.5, EntityAttributeModifier.Operation.ADD_VALUE);
            ExtraAttributeModifierComponent netheriteEnvironmentHeatResistance = new ExtraAttributeModifierComponent(0.125, EntityAttributeModifier.Operation.ADD_VALUE);

            context.modify(
                    List.of(
                            Items.NETHERITE_HELMET,
                            Items.NETHERITE_CHESTPLATE,
                            Items.NETHERITE_LEGGINGS,
                            Items.NETHERITE_BOOTS
                    ),
                    (builder, item) -> {
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, netheriteHeatResistance);
                        builder.add(SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE, netheriteEnvironmentHeatResistance);
                    }
            );
        });

        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.TURTLE_HELMET,
                    builder -> {
                        builder.add(SDataComponentTypes.HEAT_RESISTANCE, TurtleArmorEffects.HEAT_RESISTANCE);
                        builder.add(SDataComponentTypes.ENVIRONMENT_HEAT_RESISTANCE, TurtleArmorEffects.ENVIRONMENT_HEAT_RESISTANCE);
                    }
            );
        });
    }

    private HeatResistanceModifier() {

    }
}