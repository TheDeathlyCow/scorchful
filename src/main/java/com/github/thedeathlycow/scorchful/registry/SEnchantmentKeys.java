package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class SEnchantmentKeys {
    public static final RegistryKey<Enchantment> REHYDRATION = key("rehydration");

    private static RegistryKey<Enchantment> key(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Scorchful.id(name));
    }

    private SEnchantmentKeys() {

    }
}