package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevel;
import com.github.thedeathlycow.scorchful.item.component.HeatResistance;
import com.github.thedeathlycow.scorchful.item.component.SunHatRenderer;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class SDataComponentTypes {
    public static final DataComponentType<DrinkLevel> DRINK_LEVEL = register(
            "drink_level",
            builder -> builder
                    .persistent(DrinkLevel.CODEC)
                    .networkSynchronized(DrinkLevel.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static final DataComponentType<DrinkContainer> DRINK_CONTAINER = register(
            "drink_container",
            builder -> builder
                    .persistent(DrinkContainer.CODEC)
                    .networkSynchronized(DrinkContainer.PACKET_CODEC)
    );

    public static final DataComponentType<HeatResistance> HEAT_RESISTANCE = register(
            "heat_resistance",
            builder -> builder
                    .persistent(HeatResistance.CODEC)
                    .networkSynchronized(HeatResistance.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static final DataComponentType<SunHatRenderer> SUN_HAT_RENDERER = register(
            "sun_hat_renderer",
            builder -> builder
                    .persistent(SunHatRenderer.CODEC)
                    .networkSynchronized(SunHatRenderer.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful item components");

        BuiltInRegistries.DATA_COMPONENT_TYPE.addAlias(Scorchful.id("num_drinks"), Scorchful.id("drink_container"));
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
