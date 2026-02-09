package com.github.thedeathlycow.scorchful.network;

import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class SoundTemperatureEffectPacketListener implements ClientPlayNetworking.PlayPayloadHandler<TemperatureSoundEventPacket> {

    @Override
    public void receive(TemperatureSoundEventPacket payload, ClientPlayNetworking.Context context) {
        if (!ScorchfulClientConfig.getAccessibilitySettings().enableSoundTemperatureEffects()) {
            return;
        }

        context.client().execute(() -> {
            MinecraftClient client = context.client();

            if (client.world == null || client.player == null) {
                return;
            }

            client.world.playSound(
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
