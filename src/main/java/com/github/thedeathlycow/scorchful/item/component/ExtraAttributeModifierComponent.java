package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ExtraAttributeModifierComponent(
        List<Entry> modifiers
) {
    private static final Codec<ExtraAttributeModifierComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Entry.CODEC.listOf()
                            .fieldOf("modifiers")
                            .forGetter(ExtraAttributeModifierComponent::modifiers)
            ).apply(instance, ExtraAttributeModifierComponent::new)
    );

    public static final Codec<ExtraAttributeModifierComponent> CODEC = Codec.withAlternative(
            BASE_CODEC,
            Entry.CODEC.listOf(),
            ExtraAttributeModifierComponent::new
    );

    public static final PacketCodec<RegistryByteBuf, ExtraAttributeModifierComponent> PACKET_CODEC = PacketCodec.tuple(
            Entry.PACKET_CODEC.collect(PacketCodecs.toList()),
            ExtraAttributeModifierComponent::modifiers,
            ExtraAttributeModifierComponent::new
    );

    public ExtraAttributeModifierComponent(Entry... modifiers) {
        this(Arrays.stream(modifiers).toList());
    }

    public static final ExtraAttributeModifierComponent DEFAULT = new ExtraAttributeModifierComponent(
            new Entry(
                    ThermooAttributes.HEAT_RESISTANCE,
                    -0.5,
                    EntityAttributeModifier.Operation.ADD_VALUE,
                    Scorchful.id("base_heat_resistance")
            ),
            new Entry(
                    ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                    -0.125,
                    EntityAttributeModifier.Operation.ADD_VALUE,
                    Scorchful.id("base_environment_heat_resistance")
            )
    );

    public static final ExtraAttributeModifierComponent EMPTY = new ExtraAttributeModifierComponent(Collections.emptyList());

    public record Entry(
            RegistryEntry<EntityAttribute> attribute,
            double amount,
            EntityAttributeModifier.Operation operation,
            Identifier id
    ) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Registries.ATTRIBUTE.getEntryCodec()
                                .fieldOf("attribute")
                                .forGetter(Entry::attribute),
                        Codec.DOUBLE
                                .fieldOf("amount")
                                .forGetter(Entry::amount),
                        EntityAttributeModifier.Operation.CODEC
                                .fieldOf("operation")
                                .forGetter(Entry::operation),
                        Identifier.CODEC
                                .fieldOf("id")
                                .forGetter(Entry::id)
                ).apply(instance, Entry::new)
        );

        public static final PacketCodec<RegistryByteBuf, Entry> PACKET_CODEC = PacketCodec.tuple(
                EntityAttribute.PACKET_CODEC,
                Entry::attribute,
                PacketCodecs.DOUBLE,
                Entry::amount,
                EntityAttributeModifier.Operation.PACKET_CODEC,
                Entry::operation,
                Identifier.PACKET_CODEC,
                Entry::id,
                Entry::new
        );
    }
}