package com.github.thedeathlycow.scorchful.temperature.heatvision.v2;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SRegistryKeys;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
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

public record HeatVisionType(
        RegistryEntryList<Biome> biomes,
        Identifier rendererID
) {
    public static final Codec<HeatVisionType> ELEMENT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(HeatVisionType::biomes),
                    Identifier.CODEC
                            .fieldOf("renderer_id")
                            .forGetter(HeatVisionType::rendererID)
            ).apply(instance, HeatVisionType::new)
    );

    public static final Codec<RegistryEntry<HeatVisionType>> CODEC = RegistryElementCodec.of(
            SRegistryKeys.HEAT_VISION_TYPE,
            ELEMENT_CODEC
    );

    public static final Codec<HeatVisionType> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC
                            .fieldOf("renderer_id")
                            .forGetter(HeatVisionType::rendererID)
            ).apply(instance, HeatVisionType::new)
    );

    public static final PacketCodec<RegistryByteBuf, HeatVisionType> ELEMENT_PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC,
            HeatVisionType::rendererID,
            HeatVisionType::new
    );

    public static final PacketCodec<RegistryByteBuf, RegistryEntry<HeatVisionType>> PACKET_CODEC = PacketCodecs.registryEntry(
            SRegistryKeys.HEAT_VISION_TYPE,
            ELEMENT_PACKET_CODEC
    );

    public static final RegistryEntry<HeatVisionType> EMPTY = RegistryEntry.of(new HeatVisionType(Scorchful.id("empty")));

    private HeatVisionType(Identifier rendererID) {
        this(RegistryEntryList.empty(), rendererID);
    }
}