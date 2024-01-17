package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.registry.Registry;

public class STemperatureEffects {


    public static final SoundTemperatureEffect SOUND = new SoundTemperatureEffect();

    public static void registerAll() {
        register("sound", SOUND);
    }

    private static void register(String name, TemperatureEffect<?> effect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Scorchful.id(name), effect);
    }


    private STemperatureEffects() {

    }

}
