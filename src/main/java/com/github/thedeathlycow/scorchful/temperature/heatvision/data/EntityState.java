package com.github.thedeathlycow.scorchful.temperature.heatvision.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;

public record EntityState(
        EntityType<?> type,
        boolean onFire
) {
    public static final EntityState DEFAULT = new EntityState();

    public static final Codec<EntityState> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Registries.ENTITY_TYPE.getCodec()
                            .optionalFieldOf("type", DEFAULT.type)
                            .forGetter(EntityState::type),
                    Codec.BOOL
                            .optionalFieldOf("on_fire", DEFAULT.onFire)
                            .forGetter(EntityState::onFire)
            ).apply(instance, EntityState::new)
    );

    public static final PacketCodec<RegistryByteBuf, EntityState> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE),
            EntityState::type,
            PacketCodecs.BOOL,
            EntityState::onFire,
            EntityState::new
    );

    private EntityState() {
        this(EntityType.HUSK, false);
    }
}