package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public class SDataComponentTypes {

    public static final DataComponentType<DrinkLevelComponent> DRINK_LEVEL = register(
            "drink_level",
            builder -> builder
                    .persistent(DrinkLevelComponent.CODEC)
                    .networkSynchronized(DrinkLevelComponent.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static final DataComponentType<Integer> NUM_DRINKS = register(
            "num_drinks",
            builder -> builder
                    .persistent(ExtraCodecs.intRange(0, WaterSkinItem.MAX_DRINKS))
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<HeatResistanceComponent> HEAT_RESISTANCE = register(
            "heat_resistance",
            builder -> builder
                    .persistent(HeatResistanceComponent.CODEC)
                    .networkSynchronized(HeatResistanceComponent.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful item components");
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Scorchful.id(id),
                builderOperator.apply(DataComponentType.builder()).build()
        );
    }

    private SDataComponentTypes() {

    }
}
