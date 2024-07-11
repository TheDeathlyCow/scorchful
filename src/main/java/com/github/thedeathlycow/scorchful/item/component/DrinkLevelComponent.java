package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public enum DrinkLevelComponent implements StringIdentifiable {
    PARCHING(
            "parching",
            SItemTags.IS_PARCHING,
            Text.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
            ThirstConfig::getWaterFromParchingFood
    ),
    REFRESHING(
            "refreshing",
            SItemTags.IS_REFRESHING,
            Text.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromRefreshingFood
    ),
    SUSTAINING(
            "sustaining",
            SItemTags.IS_SUSTAINING,
            Text.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromSustainingFood
    ),
    HYDRATING(
            "hydrating",
            SItemTags.IS_HYDRATING,
            Text.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromHydratingFood
    );

    public static final Codec<DrinkLevelComponent> CODEC = StringIdentifiable.createCodec(DrinkLevelComponent::values);
    public static final IntFunction<DrinkLevelComponent> ID_TO_VALUE = ValueLists.createIdToValueFunction(
            DrinkLevelComponent::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO
    );
    public static final PacketCodec<ByteBuf, DrinkLevelComponent> PACKET_CODEC = PacketCodecs.indexed(
            ID_TO_VALUE, DrinkLevelComponent::ordinal
    );

    private final String name;

    private final TagKey<Item> tag;

    private final Text tooltipText;

    private final ToIntFunction<ThirstConfig> waterProvider;

    DrinkLevelComponent(String name, TagKey<Item> tag, Text tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
        this.name = name;
        this.tag = tag;
        this.tooltipText = tooltipText;
        this.waterProvider = waterProvider;
    }

    public static ItemStack applyToNewStack(ItemStack stack) {
        if (stack.contains(SDataComponentTypes.DRINK_LEVEL)) {
            return stack;
        }

        DrinkLevelComponent level = byTag(stack);
        if (level != null) {
            stack.set(SDataComponentTypes.DRINK_LEVEL, level);
        }
        return stack;
    }

    @Nullable
    public static DrinkLevelComponent byTag(ItemStack stack) {
        for (DrinkLevelComponent level : values()) {
            if (stack.isIn(level.tag)) {
                return level;
            }
        }

        return null;
    }

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
