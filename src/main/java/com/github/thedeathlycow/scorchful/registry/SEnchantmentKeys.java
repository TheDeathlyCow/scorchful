package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class SEnchantmentKeys {
    public static final ResourceKey<Enchantment> REHYDRATION = key("rehydration");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Scorchful.id(name));
    }

    private SEnchantmentKeys() {

    }
}