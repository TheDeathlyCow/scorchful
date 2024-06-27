package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.client.SandstormEffects;
import com.github.thedeathlycow.scorchful.client.SandstormSounds;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.hud.MountHealthOverlay;
import com.github.thedeathlycow.scorchful.hud.SoakingUnderlay;
import com.github.thedeathlycow.scorchful.item.QuenchingFoods;
import com.github.thedeathlycow.scorchful.item.SModelPredicates;
import com.github.thedeathlycow.scorchful.network.SoundTemperatureEffectPacketListener;
import com.github.thedeathlycow.scorchful.registry.SCutouts;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import com.github.thedeathlycow.scorchful.registry.SFeatureRenderers;
import com.github.thedeathlycow.scorchful.registry.SParticleFactories;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

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
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(SoakingUnderlay.INSTANCE);
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(BurningHeartsOverlay.INSTANCE);
        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.register(MountHealthOverlay.INSTANCE);
        ItemTooltipCallback.EVENT.register(QuenchingFoods::appendTooltip);
        ClientPlayNetworking.registerGlobalReceiver(SoundTemperatureEffect.PACKET_ID, SoundTemperatureEffectPacketListener.INSTANCE);
    }

}