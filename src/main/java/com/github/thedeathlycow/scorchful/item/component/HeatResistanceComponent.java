package com.github.thedeathlycow.scorchful.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record HeatResistanceComponent(
        double heatResistance,
        double environmentHeatResistance
) {
    public static final Codec<HeatResistanceComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("heat_resistance")
                            .forGetter(HeatResistanceComponent::heatResistance),
                    Codec.DOUBLE
                            .fieldOf("environment_heat_resistance")
                            .forGetter(HeatResistanceComponent::environmentHeatResistance)
            ).apply(instance, HeatResistanceComponent::new)
    );

    public static final PacketCodec<ByteBuf, HeatResistanceComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE,
            HeatResistanceComponent::heatResistance,
            PacketCodecs.DOUBLE,
            HeatResistanceComponent::environmentHeatResistance,
            HeatResistanceComponent::new
    );

    public static final HeatResistanceComponent DEFAULT = new HeatResistanceComponent(-0.5, -0.125);
    public static final HeatResistanceComponent VERY_PROTECTIVE = new HeatResistanceComponent(1.0, 0.25);
    public static final HeatResistanceComponent PROTECTIVE = new HeatResistanceComponent(0.5, 0.125);
    public static final HeatResistanceComponent NEUTRAL = new HeatResistanceComponent(0, 0);
    public static final HeatResistanceComponent VERY_HARMFUL = new HeatResistanceComponent(-0.5, -0.125);
}