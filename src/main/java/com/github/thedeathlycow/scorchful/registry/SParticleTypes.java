package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.particle.DustGrainParticleEffect;
import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class SParticleTypes {

    public static final ParticleType<SpurtingWaterParticleEffect> SPURTING_WATER = register(
            "spurting_water",
            FabricParticleTypes.complex(
                    SpurtingWaterParticleEffect.CODEC,
                    SpurtingWaterParticleEffect.PACKET_CODEC
            )
    );

    public static final ParticleType<DustGrainParticleEffect> DUST_GRAIN = register(
            "dust_grain",
            FabricParticleTypes.complex(
                    DustGrainParticleEffect.CODEC,
                    DustGrainParticleEffect.PACKET_CODEC
            )
    );

    public static final SimpleParticleType BAT = registerSimple("bat", FabricParticleTypes.simple());

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful particle types");
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> particle) {
        return Registry.register(Registries.PARTICLE_TYPE, Scorchful.id(name), particle);
    }

    private static SimpleParticleType registerSimple(String name, SimpleParticleType particle) {
        return Registry.register(Registries.PARTICLE_TYPE, Scorchful.id(name), particle);
    }

    private SParticleTypes() {

    }

}
