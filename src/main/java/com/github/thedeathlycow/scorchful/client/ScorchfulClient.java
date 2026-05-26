package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.client.client.SandstormEffects;
import com.github.thedeathlycow.scorchful.client.client.SandstormSounds;
import com.github.thedeathlycow.scorchful.client.client.ShaderStatusEffectManagers;
import com.github.thedeathlycow.scorchful.client.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.client.hud.MountHealthOverlay;
import com.github.thedeathlycow.scorchful.client.hud.SoakingUnderlay;
import com.github.thedeathlycow.scorchful.client.item.ItemTooltips;
import com.github.thedeathlycow.scorchful.client.item.SModelPredicates;
import com.github.thedeathlycow.scorchful.client.network.SoundTemperatureEffectPacketListener;
import com.github.thedeathlycow.scorchful.client.registry.SCutouts;
import com.github.thedeathlycow.scorchful.client.registry.SEntityModelLayers;
import com.github.thedeathlycow.scorchful.client.registry.SFeatureRenderers;
import com.github.thedeathlycow.scorchful.client.registry.SParticleFactories;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ScorchfulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
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

        ItemTooltipCallback.EVENT.register(ItemTooltips::appendDrinkTooltip);
        ItemTooltipCallback.EVENT.register(ItemTooltips::appendCoolingTooltip);

        ClientPlayNetworking.registerGlobalReceiver(
                TemperatureSoundEventPacket.PACKET_ID,
                new SoundTemperatureEffectPacketListener()
        );

        ShaderStatusEffectManagers.initialize();
    }

}