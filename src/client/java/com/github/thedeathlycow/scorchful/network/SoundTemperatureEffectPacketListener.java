package com.github.thedeathlycow.scorchful.network;

import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

public final class SoundTemperatureEffectPacketListener implements ClientPlayNetworking.PlayPayloadHandler<TemperatureSoundEventPacket> {

    @Override
    public void receive(TemperatureSoundEventPacket payload, ClientPlayNetworking.Context context) {
        if (!ScorchfulClientConfig.getAccessibilitySettings().enableSoundTemperatureEffects()) {
            return;
        }

        context.client().execute(() -> {
            Minecraft client = context.client();

            if (client.level == null || client.player == null) {
                return;
            }

            client.level.playSeededSound(
                    client.player,
                    client.player.getX(),
                    client.player.getY(),
                    client.player.getZ(),
                    payload.soundEvent(),
                    payload.category(),
                    payload.volume(),
                    payload.pitch(),
                    payload.seed()
            );
        });
    }

    public SoundTemperatureEffectPacketListener() {

    }


}
