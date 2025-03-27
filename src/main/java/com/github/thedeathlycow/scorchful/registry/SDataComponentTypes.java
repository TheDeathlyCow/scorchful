package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import com.github.thedeathlycow.scorchful.item.component.SunHatRendererComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public final class SDataComponentTypes {
    public static final ComponentType<DrinkLevelComponent> DRINK_LEVEL = register(
            "drink_level",
            builder -> builder
                    .codec(DrinkLevelComponent.CODEC)
                    .packetCodec(DrinkLevelComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<DrinkContainerComponent> DRINK_CONTAINER = register(
            "drink_container",
            builder -> builder
                    .codec(DrinkContainerComponent.CODEC)
                    .packetCodec(DrinkContainerComponent.PACKET_CODEC)
    );

    public static final ComponentType<HeatResistanceComponent> HEAT_RESISTANCE = register(
            "heat_resistance",
            builder -> builder
                    .codec(HeatResistanceComponent.CODEC)
                    .packetCodec(HeatResistanceComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<SunHatRendererComponent> SUN_HAT_RENDERER = register(
            "sun_hat_renderer",
            builder -> builder
                    .codec(SunHatRendererComponent.CODEC)
                    .packetCodec(SunHatRendererComponent.PACKET_CODEC)
                    .cache()
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful item components");

        Registries.DATA_COMPONENT_TYPE.addAlias(Scorchful.id("num_drinks"), Scorchful.id("drink_container"));
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Scorchful.id(id),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }

    private SDataComponentTypes() {

    }
}
