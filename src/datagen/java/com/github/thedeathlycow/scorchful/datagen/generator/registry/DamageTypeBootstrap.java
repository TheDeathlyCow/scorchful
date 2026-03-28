package com.github.thedeathlycow.scorchful.datagen.generator.registry;

import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public final class DamageTypeBootstrap {
    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(
                SDamageTypes.HEAT,
                new DamageType("scorchful.heat", 0f)
        );
    }

    private DamageTypeBootstrap() {

    }
}