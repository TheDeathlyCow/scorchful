package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.particle.DustGrainOptions;
import com.github.thedeathlycow.scorchful.particle.SpurtingWaterOption;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class SParticleTypes {

    public static final ParticleType<SpurtingWaterOption> SPURTING_WATER = register(
            "spurting_water",
            FabricParticleTypes.complex(
                    SpurtingWaterOption.CODEC,
                    SpurtingWaterOption.PACKET_CODEC
            )
    );

    public static final ParticleType<DustGrainOptions> DUST_GRAIN = register(
            "dust_grain",
            FabricParticleTypes.complex(
                    DustGrainOptions.CODEC,
                    DustGrainOptions.PACKET_CODEC
            )
    );

    public static final SimpleParticleType BAT = registerSimple("bat", FabricParticleTypes.simple());

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful particle types");
    }

    private static <T extends ParticleOptions> ParticleType<T> register(String name, ParticleType<T> particle) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Scorchful.id(name), particle);
    }

    private static SimpleParticleType registerSimple(String name, SimpleParticleType particle) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Scorchful.id(name), particle);
    }

    private SParticleTypes() {

    }

}
