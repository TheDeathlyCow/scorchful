package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum DrinkLevelComponent implements StringRepresentable {
    PARCHING(
            "parching",
            SItemTags.IS_PARCHING,
            Component.translatable("item.scorchful.tooltip.parching").setStyle(WaterSkinItem.PARCHING_STYLE),
            ThirstConfig::getWaterFromParchingFood
    ),
    REFRESHING(
            "refreshing",
            SItemTags.IS_REFRESHING,
            Component.translatable("item.scorchful.tooltip.refreshing").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromRefreshingFood
    ),
    SUSTAINING(
            "sustaining",
            SItemTags.IS_SUSTAINING,
            Component.translatable("item.scorchful.tooltip.sustaining").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromSustainingFood
    ),
    HYDRATING(
            "hydrating",
            SItemTags.IS_HYDRATING,
            Component.translatable("item.scorchful.tooltip.hydrating").setStyle(WaterSkinItem.TOOLTIP_STYLE),
            ThirstConfig::getWaterFromHydratingFood
    );

    public static final Codec<DrinkLevelComponent> CODEC = StringRepresentable.fromEnum(DrinkLevelComponent::values);
    public static final IntFunction<DrinkLevelComponent> ID_TO_VALUE = ByIdMap.continuous(
            DrinkLevelComponent::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO
    );
    public static final StreamCodec<ByteBuf, DrinkLevelComponent> PACKET_CODEC = ByteBufCodecs.idMapper(
            ID_TO_VALUE, DrinkLevelComponent::ordinal
    );

    private final String name;

    private final TagKey<Item> tag;

    private final Component tooltipText;

    private final ToIntFunction<ThirstConfig> waterProvider;

    DrinkLevelComponent(String name, TagKey<Item> tag, Component tooltipText, ToIntFunction<ThirstConfig> waterProvider) {
        this.name = name;
        this.tag = tag;
        this.tooltipText = tooltipText;
        this.waterProvider = waterProvider;
    }

    public static void applyToNewStack(ItemStack stack) {
        DrinkLevelComponent level = byTag(stack);
        if (level != null) {
            stack.set(SDataComponentTypes.DRINK_LEVEL, level);
        }
    }

    @Nullable
    private static DrinkLevelComponent byTag(ItemStack stack) {
        for (DrinkLevelComponent level : values()) {
            if (stack.is(level.tag)) {
                return level;
            }
        }

        return null;
    }

    public int getDrinkingWater(ThirstConfig config) {
        return this.waterProvider.applyAsInt(config);
    }

    public Component getTooltipText() {
        return tooltipText;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

}
