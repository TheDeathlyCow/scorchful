package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class SDataComponentTypes {

    public static final ComponentType<DrinkLevelComponent> DRINK_LEVEL = register(
            "drink_level",
            builder -> builder
                    .codec(DrinkLevelComponent.CODEC)
                    .packetCodec(DrinkLevelComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<Integer> NUM_DRINKS = register(
            "num_drinks",
            builder -> builder
                    .codec(Codecs.rangedInt(0, WaterSkinItem.MAX_DRINKS))
                    .packetCodec(PacketCodecs.VAR_INT)
    );

    public static final ComponentType<HeatResistanceComponent> HEAT_RESISTANCE = register(
            "heat_resistance",
            builder -> builder
                    .codec(HeatResistanceComponent.CODEC)
                    .packetCodec(HeatResistanceComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<Unit> MODIFY_CAMERA_OVERLAY_OPACITY = register(
            "modify_camera_overlay_opacity",
            builder -> builder
                    .codec(Codec.unit(Unit.INSTANCE))
                    .packetCodec(PacketCodec.unit(Unit.INSTANCE))
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful item components");
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
