package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import com.github.thedeathlycow.scorchful.item.SModelPredicates;
import com.github.thedeathlycow.scorchful.particle.SpurtingWaterParticle;
import com.github.thedeathlycow.scorchful.registry.SCutouts;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import com.github.thedeathlycow.scorchful.registry.SFeatureRenderers;
import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class ScorchfulClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SModelPredicates.onInitialize();
        SEntityModelLayers.registerAll();
        SFeatureRenderers.registerAll();
        SCutouts.registerCutouts();
        ClientTickEvents.END_WORLD_TICK.register(SandstormEffects::onClientWorldTick);

        ParticleFactoryRegistry.getInstance()
                .register(SParticleTypes.SPURTING_WATER, SpurtingWaterParticle.Factory::new);
    }

}