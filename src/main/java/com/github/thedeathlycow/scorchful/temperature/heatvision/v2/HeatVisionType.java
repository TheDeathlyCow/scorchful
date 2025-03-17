package com.github.thedeathlycow.scorchful.temperature.heatvision.v2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record HeatVisionType(
        RegistryEntryList<Biome> biomes,
        Identifier rendererID
) {
    public static final Codec<HeatVisionType> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(HeatVisionType::biomes),
                    Identifier.CODEC
                            .fieldOf("renderer_id")
                            .forGetter(HeatVisionType::rendererID)
            ).apply(instance, HeatVisionType::new)
    );

    public static final Codec<HeatVisionType> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC
                            .fieldOf("renderer_id")
                            .forGetter(HeatVisionType::rendererID)
            ).apply(instance, HeatVisionType::new)
    );

    private HeatVisionType(Identifier rendererID) {
        this(RegistryEntryList.empty(), rendererID);
    }
}