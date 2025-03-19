package com.github.thedeathlycow.scorchful.temperature.heatvision.data;

import com.github.thedeathlycow.scorchful.mixin.accessor.TrackedDataHandlerRegistryAccessor;
import com.github.thedeathlycow.scorchful.registry.SRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.collection.Weight;
import net.minecraft.util.collection.Weighted;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public record HeatVisionDefinition(
        RegistryEntryList<Biome> biomes,
        Weight weight,
        HeatVisionType renderType,
        Optional<EntityState> entityState,
        Optional<BlockState> blockState
) implements Weighted {
    public static final Codec<HeatVisionDefinition> ELEMENT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(HeatVisionDefinition::biomes),
                    Weight.CODEC
                            .fieldOf("weight")
                            .forGetter(HeatVisionDefinition::weight),
                    HeatVisionType.CODEC
                            .fieldOf("render_type")
                            .forGetter(HeatVisionDefinition::renderType),
                    EntityState.CODEC
                            .optionalFieldOf("entity_state")
                            .forGetter(HeatVisionDefinition::entityState),
                    BlockState.CODEC
                            .optionalFieldOf("block_state")
                            .forGetter(HeatVisionDefinition::blockState)
            ).apply(instance, HeatVisionDefinition::new)
    );

    public static final Codec<RegistryEntry<HeatVisionDefinition>> CODEC = RegistryElementCodec.of(
            SRegistryKeys.HEAT_VISION,
            ELEMENT_CODEC
    );

    public static final Codec<HeatVisionDefinition> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    HeatVisionType.CODEC
                            .fieldOf("render_type")
                            .forGetter(HeatVisionDefinition::renderType),
                    EntityState.CODEC
                            .optionalFieldOf("entity_state")
                            .forGetter(HeatVisionDefinition::entityState),
                    BlockState.CODEC
                            .optionalFieldOf("block_state")
                            .forGetter(HeatVisionDefinition::blockState)
            ).apply(instance, HeatVisionDefinition::new)
    );

    public static final PacketCodec<RegistryByteBuf, HeatVisionDefinition> ELEMENT_PACKET_CODEC = PacketCodec.tuple(
            HeatVisionType.PACKET_CODEC,
            HeatVisionDefinition::renderType,
            PacketCodecs.optional(EntityState.PACKET_CODEC),
            HeatVisionDefinition::entityState,
            TrackedDataHandlerRegistryAccessor.scorchful$blockStatePackCodec(),
            HeatVisionDefinition::blockState,
            HeatVisionDefinition::new
    );

    public static final PacketCodec<RegistryByteBuf, RegistryEntry<HeatVisionDefinition>> PACKET_CODEC = PacketCodecs.registryEntry(
            SRegistryKeys.HEAT_VISION,
            ELEMENT_PACKET_CODEC
    );

    public static final RegistryEntry<HeatVisionDefinition> EMPTY = RegistryEntry.of(
            new HeatVisionDefinition(HeatVisionType.EMPTY, Optional.empty(), Optional.empty())
    );

    private HeatVisionDefinition(HeatVisionType type, Optional<EntityState> entityState, Optional<BlockState> blockState) {
        this(
                RegistryEntryList.empty(),
                Weight.of(1),
                type,
                entityState,
                blockState
        );
    }

    @Override
    public Weight getWeight() {
        return this.weight;
    }
}