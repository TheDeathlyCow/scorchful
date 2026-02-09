package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.ChangeTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import net.minecraft.core.Registry;

public class STemperatureEffects {
    public static final TemperatureEffect<SoundTemperatureEffect.Config> SOUND = register(
            "sound",
            new SoundTemperatureEffect(SoundTemperatureEffect.Config.CODEC)
    );
    public static final TemperatureEffect<ChangeTemperatureEffect.Config> CHANGE_TEMPERATURE = register(
            "change_temperature",
            new ChangeTemperatureEffect(
                    ChangeTemperatureEffect.Config.CODEC
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful temperature effects");
    }

    private static <T> TemperatureEffect<T> register(String name, TemperatureEffect<T> effect) {
        return Registry.register(ThermooRegistries.TEMPERATURE_EFFECTS, Scorchful.id(name), effect);
    }

    private STemperatureEffects() {

    }
}
