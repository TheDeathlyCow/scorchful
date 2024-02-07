package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class HeatResistantArmourTagApplicator implements ModifyItemAttributeModifiersCallback {


    private final Map<ArmorItem.Type, UUID> uuidMap = Util.make(
            new EnumMap<>(ArmorItem.Type.class),
            values -> {
                values.put(ArmorItem.Type.BOOTS, UUID.fromString("96e83174-8092-4ca5-ac94-e78c9b77858c"));
                values.put(ArmorItem.Type.LEGGINGS, UUID.fromString("c900d541-3151-416a-9154-eed800782e2d"));
                values.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("a082f874-6583-49e9-a289-e983ab4bb4df"));
                values.put(ArmorItem.Type.HELMET, UUID.fromString("93fcf7fc-d984-4daf-8aee-3097a280f6da"));
            }
    );

    @Override
    public void modifyAttributeModifiers(
            ItemStack stack,
            EquipmentSlot slot,
            Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers
    ) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            ArmorItem.Type type = armorItem.getType();

            if (slot != type.getEquipmentSlot()) {
                return;
            }

            HeatingConfig config = Scorchful.getConfig().heatingConfig;
            // make negative by default
            double amount = config.getDefaultArmorHeatResistance();
            if (stack.isIn(SItemTags.HEAT_PROTECTIVE)) {
                amount = config.getProtectiveArmorHeatResistance();
            } else if (stack.isIn(SItemTags.HEAT_VERY_HARMFUL)) {
                amount = config.getHarmfulArmorHeatResistance();
            } else if (stack.isIn(SItemTags.HEAT_NEUTRAL)) {
                amount = Double.NaN;
            }

            if (!Double.isNaN(amount)) {
                attributeModifiers.put(
                        ThermooAttributes.HEAT_RESISTANCE,
                        new EntityAttributeModifier(
                                uuidMap.get(type),
                                "Heat resistance",
                                amount,
                                EntityAttributeModifier.Operation.ADDITION
                        )
                );
            }
        }
    }

    private record SlotAttributeConfig(
            UUID modifierId,
            double protectiveAmount,
            double harmfulAmount,
            double baseAmount
    ) {

    }
}
