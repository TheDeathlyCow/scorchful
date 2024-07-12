package com.github.thedeathlycow.scorchful.network;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

public final class SoundTemperatureEffectPacketListener implements ClientPlayNetworking.PlayPayloadHandler<TemperatureSoundEventPacket> {

    @Override
    public void receive(TemperatureSoundEventPacket payload, ClientPlayNetworking.Context context) {
        if (!Scorchful.getConfig().clientConfig.enableSoundTemperatureEffects()) {
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
