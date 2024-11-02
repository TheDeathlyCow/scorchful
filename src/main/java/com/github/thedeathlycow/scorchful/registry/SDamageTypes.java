package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public interface SDamageTypes {

    RegistryKey<DamageType> SCARE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Scorchful.id("scare"));

}
