package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import java.util.EnumMap;

public final class SArmorMaterials {
    public static final ArmorMaterial TURTLE = new ArmorMaterial(
            25,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 2);
                map.put(ArmorType.LEGGINGS, 5);
                map.put(ArmorType.CHESTPLATE, 6);
                map.put(ArmorType.HELMET, 2);
                map.put(ArmorType.BODY, 5);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_TURTLE,
            0.0f,
            0.0f,
            ItemTags.REPAIRS_TURTLE_HELMET,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Scorchful.id("turtle"))
    );

    private SArmorMaterials() {
    }
}
