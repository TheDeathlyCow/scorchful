package com.github.thedeathlycow.scorchful.server.network;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public record TemperatureSoundEventPacket(
        SoundEvent soundEvent,
        SoundCategory category,
        float volume,
        float pitch,
        long seed
) implements CustomPayload {

    public static final CustomPayload.Id<TemperatureSoundEventPacket> PACKET_ID = new CustomPayload.Id<>(
            Scorchful.id("temperature_sound_event")
    );

    public static final PacketCodec<PacketByteBuf, TemperatureSoundEventPacket> PACKET_CODEC = PacketCodec.tuple(
            SoundEvent.PACKET_CODEC,
            TemperatureSoundEventPacket::soundEvent,
            PacketCodecs.indexed(ord -> SoundCategory.values()[ord], SoundCategory::ordinal),
            TemperatureSoundEventPacket::category,
            PacketCodecs.FLOAT,
            TemperatureSoundEventPacket::volume,
            PacketCodecs.FLOAT,
            TemperatureSoundEventPacket::pitch,
            PacketCodecs.VAR_LONG,
            TemperatureSoundEventPacket::seed,
            TemperatureSoundEventPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
