package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.enchantment.RehydrationEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SEnchantments {
    private static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static final Enchantment REHYDRATION = new RehydrationEnchantment(Enchantment.Rarity.RARE, ALL_ARMOR);

    public static void registerAll() {
        register("rehydration", REHYDRATION);
    }

    private static void register(String name, Enchantment enchantment) {
        Registry.register(Registries.ENCHANTMENT, Scorchful.id(name), enchantment);
    }

    private SEnchantments() {
    }
}
