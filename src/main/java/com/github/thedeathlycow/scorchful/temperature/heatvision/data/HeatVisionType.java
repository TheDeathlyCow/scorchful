package com.github.thedeathlycow.scorchful.temperature.heatvision.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

public enum HeatVisionType implements StringIdentifiable {
    EMPTY("empty"),
    ENTITY("entity"),
    BLOCK("block");

    public static final Codec<HeatVisionType> CODEC = StringIdentifiable.createCodec(HeatVisionType::values);

    public static final PacketCodec<ByteBuf, HeatVisionType> PACKET_CODEC = PacketCodecs.indexed(
            i -> HeatVisionType.values()[i],
            HeatVisionType::ordinal
    );

    private final String name;

    HeatVisionType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}