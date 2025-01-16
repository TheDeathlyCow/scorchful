package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.CoolFromPantingTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import net.minecraft.registry.Registry;

public class STemperatureEffects {
    public static final SoundTemperatureEffect SOUND = new SoundTemperatureEffect(SoundTemperatureEffect.Config.CODEC);
    public static final CoolFromPantingTemperatureEffect COOL_FROM_PANTING = new CoolFromPantingTemperatureEffect(
            CoolFromPantingTemperatureEffect.Config.CODEC
    );

    public static void registerAll() {
        register("sound", SOUND);
        register("cool_from_panting", COOL_FROM_PANTING);
    }

    private static void register(String name, TemperatureEffect<?> effect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Scorchful.id(name), effect);
    }


    private STemperatureEffects() {

    }
}
