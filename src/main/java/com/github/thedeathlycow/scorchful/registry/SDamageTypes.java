package com.github.thedeathlycow.scorchful.registry;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public interface SDamageTypes {

    RegistryKey<DamageType> SCARE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.ofVanilla("mob_attack"));

}
