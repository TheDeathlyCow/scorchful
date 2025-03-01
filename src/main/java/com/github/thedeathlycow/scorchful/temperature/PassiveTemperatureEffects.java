package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class PassiveTemperatureEffects {
    public static void initialize() {
        LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE.register(PassiveTemperatureEffects::getPassiveChange);
    }

    private static int getPassiveChange(EnvironmentTickContext<LivingEntity> context) {
        LivingEntity entity = context.affected();

        // don't touch frostiful's effects
        if (entity.isSpectator() || entity.thermoo$getTemperature() < 0) {
            return 0;
        }

        int total = 0;

        ScorchfulConfig config = Scorchful.getConfig();
        total += getIcyFloorTemperatureChange(context, config);
        total -= getCoolingFromSweat(context, config);

        return total;
    }

    private static int getIcyFloorTemperatureChange(EnvironmentTickContext<LivingEntity> context, ScorchfulConfig config) {
        LivingEntity entity = context.affected();
        BlockState steppingState = entity.getSteppingBlockState();

        if (steppingState.isIn(SBlockTags.HEAVY_ICE) && entity.thermoo$isWarm()) {
            return -config.heatingConfig.getCoolingFromIce();
        }

        return 0;
    }

    private static int getCoolingFromSweat(EnvironmentTickContext<LivingEntity> context, ScorchfulConfig config) {
        LivingEntity entity = context.affected();
        if (entity.thermoo$isWet()) {
            int temperatureChange = config.thirstConfig.getTemperatureFromWetness();
            if (!context.affected().isSubmergedInWater()) {
                float efficiency = getSweatEfficiency(context, config);
                temperatureChange = MathHelper.floor(temperatureChange * efficiency);
            }
            return temperatureChange;
        }

        return 0;
    }

    private static float getSweatEfficiency(EnvironmentTickContext<LivingEntity> context, ScorchfulConfig config) {
        double relativeHumidity = context.components().getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
        if (relativeHumidity <= 0.2f) {
            return config.integrationConfig.seasonsConfig.getDrySeasonHumidBiomeSweatEfficiency();
        } else if (relativeHumidity >= 0.8f) {
            return config.integrationConfig.seasonsConfig.getWetSeasonHumidBiomeSweatEfficiency();
        } else if (relativeHumidity >= 0.6f) {
            return config.thirstConfig.getHumidBiomeSweatEfficiency();
        } else {
            return 1f;
        }
    }

    private PassiveTemperatureEffects() {

    }
}