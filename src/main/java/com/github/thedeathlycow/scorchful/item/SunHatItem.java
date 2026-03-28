package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.component.SunHatRenderer;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;

public final class SunHatItem {
    public static Item createItem(Item.Properties settings) {
        return new Item(
                settings
                        .equipmentSlot((entity, stack) -> EquipmentSlot.HEAD)
                        .attributes(SunHatItem.attributeModifiers())
                        .stacksTo(1)
                        .component(
                                DataComponents.EQUIPPABLE,
                                Equippable.builder(EquipmentSlot.HEAD)
                                        .setDamageOnHurt(false)
                                        .setCameraOverlay(SunHatRenderer.SHADE_OVERLAY_TEXTURE)
                                        .build()
                        )
                        .component(SDataComponentTypes.SUN_HAT_RENDERER, SunHatRenderer.DEFAULT)
        );
    }

    private static ItemAttributeModifiers attributeModifiers() {
        return ItemAttributeModifiers.builder()
                .add(
                        ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                        new AttributeModifier(
                                Scorchful.id("sun_hat_resistance"),
                                0.25,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.HEAD
                )
                .build();
    }

    private SunHatItem() {

    }
}
