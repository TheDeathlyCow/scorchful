package com.github.thedeathlycow.scorchful.server.network;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public record TemperatureSoundEventPacket(
        SoundEvent soundEvent,
        SoundSource category,
        float volume,
        float pitch,
        long seed
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TemperatureSoundEventPacket> PACKET_ID = new CustomPacketPayload.Type<>(
            Scorchful.id("temperature_sound_event")
    );

    public static final StreamCodec<FriendlyByteBuf, TemperatureSoundEventPacket> PACKET_CODEC = StreamCodec.composite(
            SoundEvent.DIRECT_STREAM_CODEC,
            TemperatureSoundEventPacket::soundEvent,
            ByteBufCodecs.idMapper(ord -> SoundSource.values()[ord], SoundSource::ordinal),
            TemperatureSoundEventPacket::category,
            ByteBufCodecs.FLOAT,
            TemperatureSoundEventPacket::volume,
            ByteBufCodecs.FLOAT,
            TemperatureSoundEventPacket::pitch,
            ByteBufCodecs.VAR_LONG,
            TemperatureSoundEventPacket::seed,
            TemperatureSoundEventPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
