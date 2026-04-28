package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public final class ServerPlayerEnvironmentTickListeners {
    public static void initialize() {
        ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.register(ServerPlayerEnvironmentTickListeners::getTemperatureChange);
        ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_CHANGE.register(ServerPlayerEnvironmentTickListeners::allowTemperatureChange);
    }

    private static int getTemperatureChange(EnvironmentTickContext<ServerPlayer> context) {
        if (context.affected().isSpectator()) {
            return 0;
        }

        TemperatureRecord temperature = context.components()
                .getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);

        int total = environmentTemperatureToTemperatureChange(temperature, Scorchful.getConfig().heatingConfig);

        if (context.affected().tickCount % 20 == 0 && Scorchful.LOGGER.isDebugEnabled()) {
            Scorchful.LOGGER.debug("Adding {} temperature to {}", total, context.affected().getScoreboardName());
        }

        return total;
    }

    private static TriState allowTemperatureChange(EnvironmentTickContext<ServerPlayer> context, int temperatureChange) {
        if (temperatureChange <= 0) {
            return TriState.DEFAULT;
        }

        ServerPlayer player = context.affected();
        ScorchfulConfig config = Scorchful.getConfig();

        int tickInterval = config.heatingConfig.getPassiveHeatingTickInterval();
        if (tickInterval > 1 && player.tickCount % tickInterval != 0) {
            return TriState.FALSE;
        }

        if (!config.heatingConfig.doPassiveHeating()) {
            return TriState.FALSE;
        } else {
            return TriState.of(player.thermoo$getTemperatureScale() < config.heatingConfig.getMaxPassiveHeatingScale());
        }
    }

    static int environmentTemperatureToTemperatureChange(TemperatureRecord temperature, HeatingConfig config) {
        double temperatureC = temperature.valueInUnit(TemperatureUnit.CELSIUS);

        double thresholdC = config.getMinTemperatureForHeatC();
        double degreesPerTemperatureIncrease = config.getDegreesCPerTemperatureIncrease();

        if (temperatureC < thresholdC) {
            return 0;
        }
        // Graphical proof: https://www.desmos.com/calculator/42rvcpnxwx
        double base = (temperatureC - thresholdC + degreesPerTemperatureIncrease) / degreesPerTemperatureIncrease;
        return Mth.floor(config.getEnvironmentTemperatureMultiplier() * base);
    }
}