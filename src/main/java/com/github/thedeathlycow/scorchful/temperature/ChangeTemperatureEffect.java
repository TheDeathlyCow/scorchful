package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

public class ChangeTemperatureEffect extends TemperatureEffect<ChangeTemperatureEffect.Config> {

    /**
     * @param configCodec Codec for the config type
     */
    public ChangeTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerLevel serverWorld, Config config) {
        victim.thermoo$addTemperature(config.temperatureChange(), config.heatingMode());
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.tickCount % config.interval() == 0;
    }

    public record Config(
            int temperatureChange,
            int interval,
            HeatingModes heatingMode
    ) {
        public static final Codec<HeatingModes> HEATING_MODES_CODEC = StringRepresentable.fromEnum(HeatingModes::values);

        public static final Codec<Config> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT
                                .fieldOf("temperature_change")
                                .forGetter(Config::temperatureChange),
                        ExtraCodecs.POSITIVE_INT
                                .fieldOf("interval")
                                .forGetter(Config::interval),
                        HEATING_MODES_CODEC
                                .lenientOptionalFieldOf("heating_mode", HeatingModes.ABSOLUTE)
                                .forGetter(Config::heatingMode)
                ).apply(instance, Config::new)
        );
    }
}