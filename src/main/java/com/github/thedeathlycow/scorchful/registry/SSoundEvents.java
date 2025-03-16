package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class SSoundEvents {

    public static final SoundEvent ITEM_WATER_SKIN_FILL = register("item.scorchful.water_skin.fill");
    public static final SoundEvent TEMPERATURE_EFFECT_HEARTBEAT = register("temperature_effect.scorchful.heartbeat");
    public static final SoundEvent REHYDRATE = register("enchantment.scorchful.rehydration");
    public static final SoundEvent CRIMSON_LILY_SQUELCH = register("block.scorchful.crimson_lily.squelch");
    public static final SoundEvent WEATHER_SANDSTORM = register("weather.sandstorm");
    public static final SoundEvent ENTITY_GULP = register("entity.gulp");
    public static final SoundEvent DISCOVER_VISION = register("discover_vision");
    public static final SoundEvent TEMPERATURE_EFFECT_PANT = register("temperature_effect.scorchful.pant");

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful sound events");
    }

    private static SoundEvent register(String name) {
        SoundEvent event = SoundEvent.of(Scorchful.id(name));

        return Registry.register(Registries.SOUND_EVENT, event.getId(), event);
    }

    private SSoundEvents() {

    }
}
