package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import com.github.thedeathlycow.scorchful.client.SandstormSounds;
import com.github.thedeathlycow.scorchful.hud.SoakingDisplay;
import com.github.thedeathlycow.scorchful.item.SModelPredicates;
import com.github.thedeathlycow.scorchful.registry.SCutouts;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import com.github.thedeathlycow.scorchful.registry.SFeatureRenderers;
import com.github.thedeathlycow.scorchful.registry.SParticleFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class ScorchfulClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SModelPredicates.onInitialize();
        SEntityModelLayers.registerAll();
        SFeatureRenderers.registerAll();
        SCutouts.registerCutouts();
        ClientTickEvents.END_WORLD_TICK.register(SandstormEffects::tickSandstormParticles);
        ClientTickEvents.END_WORLD_TICK.register(SandstormSounds.INSTANCE::tick);
        SParticleFactories.registerFactories();
        HudRenderCallback.EVENT.register(new SoakingDisplay());
    }

}