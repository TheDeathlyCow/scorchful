package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class SDataComponentTypes {

    public static final ComponentType<DrinkLevelComponent> DRINK_LEVEL = create(
            builder -> builder
                    .codec(DrinkLevelComponent.CODEC)
                    .packetCodec(DrinkLevelComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<Integer> NUM_DRINKS = create(
            builder -> builder
                    .codec(Codecs.rangedInt(0, WaterSkinItem.MAX_DRINKS))
                    .packetCodec(PacketCodecs.VAR_INT)
    );

    public static void initialize() {
        register("drink_level", DRINK_LEVEL);
        register("num_drinks", NUM_DRINKS);
    }

    private static <T> ComponentType<T> create(UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return builderOperator.apply(ComponentType.builder()).build();
    }

    private static <T> void register(String id, ComponentType<T> componentType) {
        Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Scorchful.id(id),
                componentType
        );
    }

    private SDataComponentTypes() {

    }
}
