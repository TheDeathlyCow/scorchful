package com.github.thedeathlycow.scorchful.temperature.heatvision.v2;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record HeatVisionDefinition(
        RegistryEntryList<Biome> biomes,
        HeatVisionType type
) {
    public static final Codec<HeatVisionDefinition> ELEMENT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(HeatVisionDefinition::biomes),
                    HeatVisionType.CODEC
                            .fieldOf("type")
                            .forGetter(HeatVisionDefinition::type)
            ).apply(instance, HeatVisionDefinition::new)
    );

    public static final Codec<RegistryEntry<HeatVisionDefinition>> CODEC = RegistryElementCodec.of(
            SRegistryKeys.HEAT_VISION,
            ELEMENT_CODEC
    );

    public static final Codec<HeatVisionDefinition> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    HeatVisionType.CODEC
                            .fieldOf("type")
                            .forGetter(HeatVisionDefinition::type)
            ).apply(instance, HeatVisionDefinition::new)
    );

    public static final PacketCodec<RegistryByteBuf, HeatVisionDefinition> ELEMENT_PACKET_CODEC = PacketCodec.tuple(
            HeatVisionType.PACKET_CODEC,
            HeatVisionDefinition::type,
            HeatVisionDefinition::new
    );

    public static final PacketCodec<RegistryByteBuf, RegistryEntry<HeatVisionDefinition>> PACKET_CODEC = PacketCodecs.registryEntry(
            SRegistryKeys.HEAT_VISION,
            ELEMENT_PACKET_CODEC
    );

    public static final RegistryEntry<HeatVisionDefinition> EMPTY = RegistryEntry.of(new HeatVisionDefinition(HeatVisionType.EMPTY));

    private HeatVisionDefinition(HeatVisionType type) {
        this(RegistryEntryList.empty(), type);
    }
}