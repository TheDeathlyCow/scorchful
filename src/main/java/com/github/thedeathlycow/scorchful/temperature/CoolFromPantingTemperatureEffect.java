package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;

public class CoolFromPantingTemperatureEffect extends TemperatureEffect<CoolFromPantingTemperatureEffect.Config> {

    /**
     * @param configCodec Codec for the config type
     */
    public CoolFromPantingTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        victim.thermoo$addTemperature(config.temperatureChange(), config.heatingMode());
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.interval() == 0;
    }

    public record Config(
            int temperatureChange,
            int interval,
            HeatingModes heatingMode
    ) {
        public static final Codec<HeatingModes> HEATING_MODES_CODEC = StringIdentifiable.createCodec(HeatingModes::values);

        public static final Codec<Config> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT
                                .fieldOf("temperature_change")
                                .forGetter(Config::temperatureChange),
                        Codecs.POSITIVE_INT
                                .fieldOf("interval")
                                .forGetter(Config::interval),
                        HEATING_MODES_CODEC
                                .lenientOptionalFieldOf("heating_mode", HeatingModes.ABSOLUTE)
                                .forGetter(Config::heatingMode)
                ).apply(instance, Config::new)
        );
    }
}