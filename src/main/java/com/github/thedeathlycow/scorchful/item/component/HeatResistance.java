package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record HeatResistance(
        double heatResistance,
        double environmentHeatResistance
) {
    public static final Codec<HeatResistance> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("heat_resistance")
                            .forGetter(HeatResistance::heatResistance),
                    Codec.DOUBLE
                            .fieldOf("environment_heat_resistance")
                            .forGetter(HeatResistance::environmentHeatResistance)
            ).apply(instance, HeatResistance::new)
    );

    public static final StreamCodec<ByteBuf, HeatResistance> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            HeatResistance::heatResistance,
            ByteBufCodecs.DOUBLE,
            HeatResistance::environmentHeatResistance,
            HeatResistance::new
    );

    public static final HeatResistance DEFAULT = new HeatResistance(-0.5, -0.125);
    public static final HeatResistance VERY_PROTECTIVE = new HeatResistance(1.0, 0.25);
    public static final HeatResistance PROTECTIVE = new HeatResistance(0.5, 0.125);
    public static final HeatResistance NEUTRAL = new HeatResistance(0, 0);
    public static final HeatResistance VERY_HARMFUL = new HeatResistance(-1, -0.25);

    public static HeatResistance get(ItemStack stack) {
        HeatResistance component = stack.get(SDataComponentTypes.HEAT_RESISTANCE);
        return component != null ? component : byTag(stack);
    }

    public static HeatResistance byTag(ItemStack stack) {
        if (stack.is(SItemTags.HEAT_RESISTANCE_MODIFIED)) {
            if (stack.is(SItemTags.VERY_PROTECTIVE_HEAT_RESISTANCE)) {
                return VERY_PROTECTIVE;
            } else if (stack.is(SItemTags.PROTECTIVE_HEAT_RESISTANCE)) {
                return PROTECTIVE;
            } else if (stack.is(SItemTags.VERY_HARMFUL_HEAT_RESISTANCE)) {
                return VERY_HARMFUL;
            } else if (stack.is(SItemTags.NEUTRAL_HEAT_RESISTANCE)) {
                return NEUTRAL;
            } else {
                return DEFAULT;
            }
        } else {
            return HeatResistance.NEUTRAL;
        }
    }
}