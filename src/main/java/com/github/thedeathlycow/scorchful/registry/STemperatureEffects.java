package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.temperature.effect.ChangeTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.effect.SoundTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.effect.WolfPantSoundEffect;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public class STemperatureEffects {
    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful temperature effects");

        register("sound", SoundTemperatureEffect.CODEC);
        register("change_temperature", ChangeTemperatureEffect.CODEC);
        register("wolf_pant_sound", WolfPantSoundEffect.CODEC);
    }

    private static void register(String name, MapCodec<? extends TemperatureEffect> effect) {
        Registry.register(ThermooBuiltInRegistries.TEMPERATURE_EFFECT_TYPE, Scorchful.id(name), effect);
    }

    private STemperatureEffects() {

    }
}
