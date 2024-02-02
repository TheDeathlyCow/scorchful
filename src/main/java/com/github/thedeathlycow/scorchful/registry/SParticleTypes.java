package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SParticleTypes {

    public static final DefaultParticleType SPURTING_WATER = FabricParticleTypes.simple();

    public static void registerAll() {
        register("spurting_water", SPURTING_WATER);
    }

    private static void register(String name, ParticleType<?> particle) {
        Registry.register(Registries.PARTICLE_TYPE, Scorchful.id(name), particle);
    }

    private SParticleTypes() {

    }

}
