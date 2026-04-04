package com.github.thedeathlycow.scorchful.client.registry;

import com.github.thedeathlycow.scorchful.client.particle.BatParticle;
import com.github.thedeathlycow.scorchful.client.particle.DustGrainParticle;
import com.github.thedeathlycow.scorchful.client.particle.SpurtingWaterParticle;
import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

@Environment(EnvType.CLIENT)
public class SParticleFactories {

    public static void registerFactories() {
        ParticleProviderRegistry instance = ParticleProviderRegistry.getInstance();
        instance.register(SParticleTypes.SPURTING_WATER, SpurtingWaterParticle.Factory::new);
        instance.register(SParticleTypes.DUST_GRAIN, DustGrainParticle.Factory::new);
        instance.register(SParticleTypes.BAT, BatParticle.Factory::new);
    }

    private SParticleFactories() {

    }

}
