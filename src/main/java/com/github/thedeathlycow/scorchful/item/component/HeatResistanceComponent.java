package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

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

    public static final StreamCodec<ByteBuf, HeatResistanceComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            HeatResistanceComponent::heatResistance,
            ByteBufCodecs.DOUBLE,
            HeatResistanceComponent::environmentHeatResistance,
            HeatResistanceComponent::new
    );

    public static final HeatResistanceComponent DEFAULT = new HeatResistanceComponent(-0.5, -0.125);
    public static final HeatResistanceComponent VERY_PROTECTIVE = new HeatResistanceComponent(1.0, 0.25);
    public static final HeatResistanceComponent PROTECTIVE = new HeatResistanceComponent(0.5, 0.125);
    public static final HeatResistanceComponent NEUTRAL = new HeatResistanceComponent(0, 0);
    public static final HeatResistanceComponent VERY_HARMFUL = new HeatResistanceComponent(-1, -0.25);

    public static HeatResistanceComponent get(ItemStack stack) {
        HeatResistanceComponent component = stack.get(SDataComponentTypes.HEAT_RESISTANCE);
        return component != null ? component : byTag(stack);
    }

    public static HeatResistanceComponent byTag(ItemStack stack) {
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
            return HeatResistanceComponent.NEUTRAL;
        }
    }
}