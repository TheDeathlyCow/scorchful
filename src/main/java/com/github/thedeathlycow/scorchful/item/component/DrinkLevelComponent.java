package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public enum DrinkLevelComponent implements StringIdentifiable {
    PARCHING(
            "parching",
            Text.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
            ThirstConfig::getWaterFromParchingFood
    ),
    REFRESHING(
            "refreshing",
            Text.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromRefreshingFood
    ),
    SUSTAINING(
            "sustaining",
            Text.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromSustainingFood
    ),
    HYDRATING(
            "hydrating",
            Text.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromHydratingFood
    );

    private final String name;

    private final Text tooltipText;

    private final ToIntFunction<ThirstConfig> waterProvider;

    DrinkLevelComponent(String name, Text tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
        this.name = name;
        this.tooltipText = tooltipText;
        this.waterProvider = waterProvider;
    }

    public static final Codec<DrinkLevelComponent> CODEC = StringIdentifiable.createCodec(DrinkLevelComponent::values);
    public static final IntFunction<DrinkLevelComponent> ID_TO_VALUE = ValueLists.createIdToValueFunction(
            DrinkLevelComponent::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO
    );
    public static final PacketCodec<ByteBuf, DrinkLevelComponent> PACKET_CODEC = PacketCodecs.indexed(
            ID_TO_VALUE, DrinkLevelComponent::ordinal
    );

    public int getDrinkingWater(ThirstConfig config) {
        return this.waterProvider.applyAsInt(config);
    }

    public Text getTooltipText() {
        return tooltipText;
    }

    @Override
    public String asString() {
        return this.name;
    }

}
