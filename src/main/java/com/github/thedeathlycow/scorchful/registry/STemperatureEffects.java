package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.ChangeTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import net.minecraft.registry.Registry;

public class STemperatureEffects {
    public static final SoundTemperatureEffect SOUND = new SoundTemperatureEffect(SoundTemperatureEffect.Config.CODEC);
    public static final ChangeTemperatureEffect CHANGE_TEMPERATURE = new ChangeTemperatureEffect(
            ChangeTemperatureEffect.Config.CODEC
    );

    public static void registerAll() {
        register("sound", SOUND);
        register("change_temperature", CHANGE_TEMPERATURE);
    }

    private static void register(String name, TemperatureEffect<?> effect) {
        Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Scorchful.id(name), effect);
    }


    private STemperatureEffects() {

    }
}
