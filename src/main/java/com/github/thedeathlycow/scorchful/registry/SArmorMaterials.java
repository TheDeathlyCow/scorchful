package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SArmorMaterials {

    public static final RegistryEntry<ArmorMaterial> STRAW = register(
            "straw",
            Util.make(
                    new EnumMap<>(ArmorItem.Type.class),
                    map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 1);
                        map.put(ArmorItem.Type.CHESTPLATE, 1);
                        map.put(ArmorItem.Type.HELMET, 1);
                    }
            ),
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            () -> Ingredient.ofItems(Items.WHEAT)
    );

    public static final RegistryEntry<ArmorMaterial> TURTLE = register(
            "turtle",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 5);
            }),
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
            0.0F,
            0.0F,
            () -> Ingredient.ofItems(Items.TURTLE_SCUTE)
    );

    public static void initialize() {
        // load this class
    }


    private static RegistryEntry<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            RegistryEntry<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Scorchful.id(id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static RegistryEntry<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            RegistryEntry<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, defense.get(type));
        }

        return Registry.registerReference(
                Registries.ARMOR_MATERIAL,
                Scorchful.id(id),
                new ArmorMaterial(
                        enumMap,
                        enchantability,
                        equipSound,
                        repairIngredient,
                        layers,
                        toughness,
                        knockbackResistance
                )
        );
    }

}
