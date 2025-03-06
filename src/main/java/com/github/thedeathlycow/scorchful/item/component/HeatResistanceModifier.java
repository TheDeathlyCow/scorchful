package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.Map;

public final class HeatResistanceModifier {
    private static final Map<EquipmentSlot, Identifier> HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);
    private static final Map<EquipmentSlot, Identifier> ENVIRONMENT_HEAT_RESISTANCE_ID = new EnumMap<>(EquipmentSlot.class);

    public static final double BASE_HEAT_RESISTANCE = 0.5;
    public static final double BASE_ENVIRONMENT_HEAT_RESISTANCE = 0.125;

    public static void initialize() {
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(stack -> {
            if (stack.isIn(ConventionalItemTags.ARMORS) && !stack.contains(SDataComponentTypes.HEAT_RESISTANCE_LEVEL)) {
                stack.set(SDataComponentTypes.HEAT_RESISTANCE_LEVEL, HeatResistanceLevelComponent.forStack(stack));
            }
        });
        ModifyItemAttributeModifiersCallback.EVENT.register(
                (stack, builder) -> {
                    if (stack.getItem() instanceof ArmorItem armorItem) {
                        CombatConfig config = Scorchful.getConfig().combatConfig;
                        HeatResistanceLevelComponent level = stack.getOrDefault(SDataComponentTypes.HEAT_RESISTANCE_LEVEL, HeatResistanceLevelComponent.HARMFUL);

                        double multiplier = level.getMultiplier(config);
                        if (multiplier == 0) {
                            return;
                        }

                        EquipmentSlot slot = armorItem.getSlotType();
                        AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(slot);

                        builder.add(
                                ThermooAttributes.HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        HEAT_RESISTANCE_ID.computeIfAbsent(slot, s -> Scorchful.id("base_heat_resistance/" + s.asString())),
                                        BASE_HEAT_RESISTANCE * multiplier,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                modifierSlot
                        );

                        builder.add(
                                ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                                new EntityAttributeModifier(
                                        ENVIRONMENT_HEAT_RESISTANCE_ID.computeIfAbsent(slot, s -> Scorchful.id("base_environment_heat_resistance/" + s.asString())),
                                        BASE_ENVIRONMENT_HEAT_RESISTANCE * multiplier,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                modifierSlot
                        );
                    }
                }
        );
    }

    private HeatResistanceModifier() {

    }
}