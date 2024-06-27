package com.github.thedeathlycow.scorchful.network;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

public final class SoundTemperatureEffectPacketListener implements ClientPlayNetworking.PlayChannelHandler {

    public static final SoundTemperatureEffectPacketListener INSTANCE = new SoundTemperatureEffectPacketListener();

    @Override
    public void receive(
            MinecraftClient client,
            ClientPlayNetworkHandler handler,
            PacketByteBuf buf,
            PacketSender responseSender
    ) {
        if (!Scorchful.getConfig().clientConfig.enableSoundTemperatureEffects()) {
            return;
        }

        SoundEvent sound = SoundEvent.fromBuf(buf);
        SoundCategory category = buf.readEnumConstant(SoundCategory.class);
        float volume = buf.readFloat();
        float pitch = buf.readFloat();
        long seed = buf.readLong();

        client.execute(() -> {

            if (client.world == null || client.player == null) {
                return;
            }

            client.world.playSound(
                    client.player,
                    client.player.getX(),
                    client.player.getY(),
                    client.player.getZ(),
                    sound,
                    category,
                    volume,
                    pitch,
                    seed
            );
        });
    }

    private SoundTemperatureEffectPacketListener() {

    }
}
