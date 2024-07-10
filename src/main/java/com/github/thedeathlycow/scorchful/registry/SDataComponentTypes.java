package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.component.DrinkComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class SDataComponentTypes {

    public static final ComponentType<DrinkComponent> DRINK = create(
            builder -> builder
                    .codec(DrinkComponent.CODEC)
                    .packetCodec(DrinkComponent.PACKET_CODEC)
                    .cache()
    );

    public static void initialize() {
        register("drink", DRINK);
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
