package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class SArmorMaterials {

    public static final Holder<ArmorMaterial> STRAW = register(
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
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            () -> Ingredient.of(Items.WHEAT)
    );

    public static final Holder<ArmorMaterial> TURTLE = register(
            "turtle",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 5);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_TURTLE,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.TURTLE_SCUTE)
    );

    public static void initialize() {
        // load this class
    }


    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Scorchful.id(id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, defense.get(type));
        }

        return Registry.registerForHolder(
                BuiltInRegistries.ARMOR_MATERIAL,
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
