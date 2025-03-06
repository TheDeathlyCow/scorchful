package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

public enum LegacyHeatResistanceLevel implements StringIdentifiable {
    VERY_PROTECTIVE(
            "very_protective",
            ArmorMaterialTags.VERY_RESISTANT_TO_HEAT,
            new ExtraAttributeModifierComponent(1, EntityAttributeModifier.Operation.ADD_VALUE),
            new ExtraAttributeModifierComponent(0.25, EntityAttributeModifier.Operation.ADD_VALUE)
    ),
    PROTECTIVE(
            "protective",
            ArmorMaterialTags.RESISTANT_TO_HEAT,
            new ExtraAttributeModifierComponent(0.5, EntityAttributeModifier.Operation.ADD_VALUE),
            new ExtraAttributeModifierComponent(0.125, EntityAttributeModifier.Operation.ADD_VALUE)
    ),
    NEUTRAL(
            "neutral",
            SArmorMaterialTags.HEAT_NEUTRAL,
            new ExtraAttributeModifierComponent(0, EntityAttributeModifier.Operation.ADD_VALUE),
            new ExtraAttributeModifierComponent(0, EntityAttributeModifier.Operation.ADD_VALUE)
    ),
    VERY_HARMFUL(
            "very_harmful",
            ArmorMaterialTags.VERY_WEAK_TO_HEAT,
            new ExtraAttributeModifierComponent(-1, EntityAttributeModifier.Operation.ADD_VALUE),
            new ExtraAttributeModifierComponent(-0.25, EntityAttributeModifier.Operation.ADD_VALUE)
    );

    public static final Codec<LegacyHeatResistanceLevel> CODEC = StringIdentifiable.createCodec(
            LegacyHeatResistanceLevel::values
    );
    public static final PacketCodec<ByteBuf, LegacyHeatResistanceLevel> PACKET_CODEC = PacketCodecs.indexed(
            i -> values()[i],
            Enum::ordinal
    );

    private final String name;
    private final TagKey<ArmorMaterial> armorMaterialTag;
    private final ExtraAttributeModifierComponent heatResistance;
    private final ExtraAttributeModifierComponent environmentHeatResistance;

    LegacyHeatResistanceLevel(String name, TagKey<ArmorMaterial> armorMaterialTag, ExtraAttributeModifierComponent heatResistance, ExtraAttributeModifierComponent environmentHeatResistance) {
        this.name = name;
        this.armorMaterialTag = armorMaterialTag;
        this.heatResistance = heatResistance;
        this.environmentHeatResistance = environmentHeatResistance;
    }

    @Nullable
    public static LegacyHeatResistanceLevel forItem(Item item) {
        if (item instanceof ArmorItem armor) {
            for (LegacyHeatResistanceLevel level : values()) {
                if (armor.getMaterial().isIn(level.armorMaterialTag)) {
                    return level;
                }
            }
        }

        return null;
    }

    public ExtraAttributeModifierComponent getHeatResistance() {
        return heatResistance;
    }

    public ExtraAttributeModifierComponent getEnvironmentHeatResistance() {
        return environmentHeatResistance;
    }

    @Override
    public String asString() {
        return this.name;
    }
}