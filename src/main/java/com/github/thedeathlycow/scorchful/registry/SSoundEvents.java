package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class SSoundEvents {

    public static final SoundEvent ITEM_WATER_SKIN_FILL = register("item.water_skin.fill");
    public static final SoundEvent TEMPERATURE_EFFECT_HEARTBEAT = register("temperature_effect.heartbeat");
    public static final SoundEvent REHYDRATE = register("enchantment.rehydration");
    public static final SoundEvent CRIMSON_LILY_SQUELCH = register("block.crimson_lily.squelch");
    public static final SoundEvent WEATHER_SANDSTORM = register("weather.sandstorm");
    public static final SoundEvent ENTITY_GULP = register("entity.gulp");
    public static final SoundEvent DISCOVER_VISION = register("discover_vision");
    public static final SoundEvent TEMPERATURE_EFFECT_PANT = register("temperature_effect.pant");

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful sound events");
        BuiltInRegistries.SOUND_EVENT.addAlias(Scorchful.id("temperature_effect.scorchful.heartbeat"), TEMPERATURE_EFFECT_HEARTBEAT.location());
        BuiltInRegistries.SOUND_EVENT.addAlias(Scorchful.id("item.scorchful.water_skin.fill"), ITEM_WATER_SKIN_FILL.location());
        BuiltInRegistries.SOUND_EVENT.addAlias(Scorchful.id("temperature_effect.scorchful.pant"), TEMPERATURE_EFFECT_PANT.location());
        BuiltInRegistries.SOUND_EVENT.addAlias(Scorchful.id("enchantment.scorchful.rehydration"), REHYDRATE.location());
        BuiltInRegistries.SOUND_EVENT.addAlias(Scorchful.id("block.scorchful.crimson_lily.squelch"), CRIMSON_LILY_SQUELCH.location());
    }

    private static SoundEvent register(String name) {
        SoundEvent event = SoundEvent.createVariableRangeEvent(Scorchful.id(name));

        return Registry.register(BuiltInRegistries.SOUND_EVENT, event.location(), event);
    }

    private SSoundEvents() {

    }
}
