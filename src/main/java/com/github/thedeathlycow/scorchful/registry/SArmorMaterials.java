package com.github.thedeathlycow.scorchful.registry;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentModels;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public final class SArmorMaterials {

    public static final ArmorMaterial TURTLE = new ArmorMaterial(
            25,
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 2);
                map.put(EquipmentType.LEGGINGS, 5);
                map.put(EquipmentType.CHESTPLATE, 6);
                map.put(EquipmentType.HELMET, 2);
                map.put(EquipmentType.BODY, 5);
            }),
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            0.0f,
            0.0f,
            ItemTags.REPAIRS_TURTLE_HELMET,
            EquipmentModels.TURTLE_SCUTE
    );

    public static void initialize() {
        // load this class

    }
}
