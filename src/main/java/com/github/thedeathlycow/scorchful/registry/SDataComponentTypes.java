package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.component.HeatResistanceComponent;
import com.github.thedeathlycow.scorchful.item.component.SunHatRendererComponent;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class SDataComponentTypes {
    public static final DataComponentType<DrinkLevelComponent> DRINK_LEVEL = register(
            "drink_level",
            builder -> builder
                    .persistent(DrinkLevelComponent.CODEC)
                    .networkSynchronized(DrinkLevelComponent.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static final DataComponentType<DrinkContainerComponent> DRINK_CONTAINER = register(
            "drink_container",
            builder -> builder
                    .persistent(DrinkContainerComponent.CODEC)
                    .networkSynchronized(DrinkContainerComponent.PACKET_CODEC)
    );

    public static final DataComponentType<HeatResistanceComponent> HEAT_RESISTANCE = register(
            "heat_resistance",
            builder -> builder
                    .persistent(HeatResistanceComponent.CODEC)
                    .networkSynchronized(HeatResistanceComponent.PACKET_CODEC)
                    .cacheEncoding()
    );

    public static final DataComponentType<SunHatRendererComponent> SUN_HAT_RENDERER = register(
            "sun_hat_renderer",
            builder -> builder
                    .persistent(SunHatRendererComponent.CODEC)
                    .networkSynchronized(SunHatRendererComponent.PACKET_CODEC)
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
