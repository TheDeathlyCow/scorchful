package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.particle.BatParticle;
import com.github.thedeathlycow.scorchful.particle.DustGrainParticle;
import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class SParticleFactories {

    public static void registerFactories() {
        ParticleFactoryRegistry instance = ParticleFactoryRegistry.getInstance();
        instance.register(SParticleTypes.SPURTING_WATER, SpurtingWaterParticle.Factory::new);
        instance.register(SParticleTypes.DUST_GRAIN, DustGrainParticle.Factory::new);
        instance.register(SParticleTypes.BAT, BatParticle.Factory::new);
    }

    private SParticleFactories() {

    }

}
