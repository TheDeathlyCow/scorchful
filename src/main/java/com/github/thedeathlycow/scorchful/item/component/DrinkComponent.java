package com.github.thedeathlycow.scorchful.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;

public record DrinkComponent(
        int water,
        Optional<ItemStack> drinkingConvertsTo
) {
    public static final DrinkComponent DEFAULT = new DrinkComponent(0, Optional.empty());

    public static final Codec<DrinkComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codecs.NONNEGATIVE_INT
                                    .fieldOf("water")
                                    .forGetter(DrinkComponent::water),
                            ItemStack.UNCOUNTED_CODEC
                                    .optionalFieldOf("drinking_converts_to")
                                    .forGetter(DrinkComponent::drinkingConvertsTo)
                    )
                    .apply(instance, DrinkComponent::new)
    );
    public static final PacketCodec<RegistryByteBuf, DrinkComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            DrinkComponent::water,
            ItemStack.PACKET_CODEC.collect(PacketCodecs::optional),
            DrinkComponent::drinkingConvertsTo,
            DrinkComponent::new
    );
}
