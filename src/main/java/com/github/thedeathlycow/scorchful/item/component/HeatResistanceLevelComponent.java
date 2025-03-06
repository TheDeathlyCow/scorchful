package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public enum HeatResistanceLevelComponent implements StringIdentifiable {
    VERY_PROTECTIVE(
            "very_protective",
            ArmorMaterialTags.VERY_RESISTANT_TO_HEAT,
            SItemTags.IS_VERY_PROTECTIVE_HEAT_RESISTANCE,
            CombatConfig::getVeryProtectiveArmorHeatResistanceMultiplier
    ),
    PROTECTIVE(
            "protective",
            ArmorMaterialTags.RESISTANT_TO_HEAT,
            SItemTags.IS_PROTECTIVE_HEAT_RESISTANCE,
            CombatConfig::getProtectiveArmorHeatResistanceMultiplier
    ),
    NEUTRAL(
            "neutral",
            SArmorMaterialTags.HEAT_NEUTRAL,
            SItemTags.IS_NEUTRAL_HEAT_RESISTANCE,
            c -> 0
    ),
    HARMFUL(
            "harmful",
            s -> true,
            CombatConfig::getDefaultArmorHeatResistanceMultiplier
    ),
    VERY_HARMFUL(
            "very_harmful",
            ArmorMaterialTags.VERY_WEAK_TO_HEAT,
            SItemTags.IS_VERY_WEAK_HEAT_RESISTANCE,
            CombatConfig::getVeryHarmfulArmorHeatResistanceMultiplier
    );

    public static final Codec<HeatResistanceLevelComponent> CODEC = StringIdentifiable.createCodec(
            HeatResistanceLevelComponent::values
    );
    public static final PacketCodec<ByteBuf, HeatResistanceLevelComponent> PACKET_CODEC = PacketCodecs.indexed(
            i -> values()[i],
            Enum::ordinal
    );

    private final String name;

    private final Predicate<ItemStack> appliesTo;

    private final ToDoubleFunction<CombatConfig> multiplier;

    HeatResistanceLevelComponent(String name, TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag, ToDoubleFunction<CombatConfig> heatResistanceProvider) {
        this(name, createTagPredicate(armorMaterialTag, itemTag), heatResistanceProvider);
    }

    HeatResistanceLevelComponent(String name, Predicate<ItemStack> appliesTo, ToDoubleFunction<CombatConfig> multiplier) {
        this.name = name;
        this.appliesTo = appliesTo;
        this.multiplier = multiplier;
    }

    public static HeatResistanceLevelComponent forStack(ItemStack stack) {
        for (HeatResistanceLevelComponent level : values()) {
            if (level != HARMFUL && level.appliesTo.test(stack)) {
                return level;
            }
        }
        return HARMFUL;
    }

    public double getMultiplier(CombatConfig config) {
        return this.multiplier.applyAsDouble(config);
    }

    private static Predicate<ItemStack> createTagPredicate(TagKey<ArmorMaterial> armorMaterialTag, TagKey<Item> itemTag) {
        return stack -> stack.isIn(itemTag)
                || (stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial().isIn(armorMaterialTag));
    }

    @Override
    public String asString() {
        return this.name;
    }
}