package com.github.thedeathlycow.scorchful.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ExtraAttributeModifierComponent(
        double amount,
        EntityAttributeModifier.Operation operation
) {
    public static final Codec<ExtraAttributeModifierComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("amount")
                            .forGetter(ExtraAttributeModifierComponent::amount),
                    EntityAttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(ExtraAttributeModifierComponent::operation)
            ).apply(instance, ExtraAttributeModifierComponent::new)
    );

    public static final PacketCodec<ByteBuf, ExtraAttributeModifierComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE,
            ExtraAttributeModifierComponent::amount,
            EntityAttributeModifier.Operation.PACKET_CODEC,
            ExtraAttributeModifierComponent::operation,
            ExtraAttributeModifierComponent::new
    );

    public static final ExtraAttributeModifierComponent DEFAULT_HEAT_RESISTANCE = new ExtraAttributeModifierComponent(
            -0.5,
            EntityAttributeModifier.Operation.ADD_VALUE
    );

    public static final ExtraAttributeModifierComponent DEFAULT_ENVIRONMENT_HEAT_RESISTANCE = new ExtraAttributeModifierComponent(
            -0.125,
            EntityAttributeModifier.Operation.ADD_VALUE
    );

    public static final ExtraAttributeModifierComponent NO_RESISTANCE = new ExtraAttributeModifierComponent(
            0,
            EntityAttributeModifier.Operation.ADD_VALUE
    );
}