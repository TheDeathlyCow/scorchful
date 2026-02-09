package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.TemperatureConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class PassiveTemperatureEffects {
    private static final double LOW_HUMIDITY = 0.2;
    private static final double HIGH_HUMIDITY = 0.65;
    private static final double VERY_HIGH_HUMIDITY = 0.8;

    public static void initialize() {
        LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE.register(PassiveTemperatureEffects::getPassiveChange);
    }

    private static int getPassiveChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity entity = context.affected();

        // don't touch frostiful's effects
        if (entity.isSpectator() || entity.thermoo$getTemperature() < 0) {
            return 0;
        }

        int total = 0;

        TemperatureConfig config = ScorchfulConfig.getTemperatureConfig();
        total += getIcyFloorTemperatureChange(context, config);
        total += getTemperatureFromSweat(context, config);

        return total;
    }

    private static int getIcyFloorTemperatureChange(EnvironmentTickContext<? extends LivingEntity> context, TemperatureConfig config) {
        LivingEntity entity = context.affected();
        BlockState steppingState = entity.getBlockStateOn();

        if (steppingState.is(SBlockTags.HEAVY_ICE) && entity.thermoo$isWarm()) {
            return config.getIceCooling();
        }

        return 0;
    }

    private static int getTemperatureFromSweat(EnvironmentTickContext<? extends LivingEntity> context, TemperatureConfig config) {
        LivingEntity entity = context.affected();
        if (entity.thermoo$isWet()) {
            int temperatureChange = config.getTemperatureFromWetness();
            if (!context.affected().isUnderWater()) {
                float efficiency = getSweatEfficiency(context, config);
                temperatureChange = Mth.floor(temperatureChange * efficiency);
            }
            return temperatureChange;
        }

        return 0;
    }

    private static float getSweatEfficiency(EnvironmentTickContext<? extends LivingEntity> context, TemperatureConfig config) {
        double relativeHumidity = context.components().getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        if (relativeHumidity <= LOW_HUMIDITY) {
            return config.getAridBiomeSweatEfficiency();
        } else if (relativeHumidity >= VERY_HIGH_HUMIDITY) {
            return config.getExtraHumidBiomeSweatEfficiency();
        } else if (relativeHumidity >= HIGH_HUMIDITY) {
            return config.getHumidBiomeSweatEfficiency();
        } else {
            return 1f;
        }
    }

    private PassiveTemperatureEffects() {

    }
}