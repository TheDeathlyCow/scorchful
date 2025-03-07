package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SArmorMaterialTags;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
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
            TurtleArmorEffects.EXTRA_ATTRIBUTES
    ),
    PROTECTIVE(
            "protective",
            ArmorMaterialTags.RESISTANT_TO_HEAT,
            new ExtraAttributeModifierComponent(
                    new ExtraAttributeModifierComponent.Entry(
                            ThermooAttributes.HEAT_RESISTANCE,
                            0.5,
                            EntityAttributeModifier.Operation.ADD_VALUE,
                            Scorchful.id("base_heat_resistance")
                    ),
                    new ExtraAttributeModifierComponent.Entry(
                            ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                            0.125,
                            EntityAttributeModifier.Operation.ADD_VALUE,
                            Scorchful.id("base_environment_heat_resistance")
                    )
            )
    ),
    NEUTRAL(
            "neutral",
            SArmorMaterialTags.HEAT_NEUTRAL,
            ExtraAttributeModifierComponent.EMPTY
    ),
    VERY_HARMFUL(
            "very_harmful",
            ArmorMaterialTags.VERY_WEAK_TO_HEAT,
            new ExtraAttributeModifierComponent(
                    new ExtraAttributeModifierComponent.Entry(
                            ThermooAttributes.HEAT_RESISTANCE,
                            -1.0,
                            EntityAttributeModifier.Operation.ADD_VALUE,
                            Scorchful.id("base_heat_resistance")
                    ),
                    new ExtraAttributeModifierComponent.Entry(
                            ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                            -0.25,
                            EntityAttributeModifier.Operation.ADD_VALUE,
                            Scorchful.id("base_environment_heat_resistance")
                    )
            )
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
    private final ExtraAttributeModifierComponent extraAttributeModifiers;

    LegacyHeatResistanceLevel(
            String name,
            TagKey<ArmorMaterial> armorMaterialTag,
            ExtraAttributeModifierComponent extraAttributeModifiers
    ) {
        this.name = name;
        this.armorMaterialTag = armorMaterialTag;
        this.extraAttributeModifiers = extraAttributeModifiers;
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

    public ExtraAttributeModifierComponent getExtraAttributeModifiers() {
        return extraAttributeModifiers;
    }

    @Override
    public String asString() {
        return this.name;
    }
}