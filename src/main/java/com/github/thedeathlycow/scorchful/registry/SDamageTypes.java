package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class SDamageTypes {
    public static final ResourceKey<DamageType> HEAT = key("heat");
    public static final ResourceKey<DamageType> SUFFOCATE = key("suffocate");

    private static ResourceKey<DamageType> key(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Scorchful.id(path));
    }

    private SDamageTypes() {

    }
}
