package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class SDamageTypes {
    public static final RegistryKey<DamageType> HEAT = key("heat");

    private static RegistryKey<DamageType> key(String path) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Scorchful.id(path));
    }

    private SDamageTypes() {

    }
}
