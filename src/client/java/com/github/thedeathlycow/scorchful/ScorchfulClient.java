package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.client.ShaderStatusEffectManagers;
import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.hud.BurningHeartsOverlay;
import com.github.thedeathlycow.scorchful.hud.MountHealthOverlay;
import com.github.thedeathlycow.scorchful.hud.SoakingUnderlay;
import com.github.thedeathlycow.scorchful.item.CoolingItemTooltip;
import com.github.thedeathlycow.scorchful.item.WaterSkinIsEmptyProperty;
import com.github.thedeathlycow.scorchful.network.SoundTemperatureEffectPacketListener;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;

@Environment(EnvType.CLIENT)
public class ScorchfulClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScorchfulClientConfig.initialize();
        SEntityModelLayers.registerAll();
        SFeatureRenderers.registerAll();
        SCutouts.registerCutouts();

        SParticleFactories.registerFactories();

        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(SoakingUnderlay.INSTANCE);
        StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR.register(BurningHeartsOverlay.INSTANCE);
        StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR.register(MountHealthOverlay.INSTANCE);

        ItemComponentTooltipProviderRegistry.addFirst(SDataComponentTypes.DRINK_CONTAINER);
        ItemComponentTooltipProviderRegistry.addLast(SDataComponentTypes.DRINK_LEVEL);
        ItemComponentTooltipProviderRegistry.addLast(SDataComponentTypes.SUN_HAT_RENDERER);
        ItemTooltipCallback.EVENT.register(new CoolingItemTooltip());

        ClientPlayNetworking.registerGlobalReceiver(
                TemperatureSoundEventPacket.PACKET_ID,
                new SoundTemperatureEffectPacketListener()
        );

        ShaderStatusEffectManagers.initialize();

        ConditionalItemModelProperties.ID_MAPPER.put(Scorchful.id("water_skin/is_empty"), WaterSkinIsEmptyProperty.CODEC);
    }

}