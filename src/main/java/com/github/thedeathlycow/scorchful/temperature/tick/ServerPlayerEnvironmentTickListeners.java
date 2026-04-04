package com.github.thedeathlycow.scorchful.temperature.tick;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.TemperatureConfig;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents;
import dev.yumi.commons.TriState;
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

        int total = environmentTemperatureToTemperatureChange(temperature, ScorchfulConfig.getTemperatureConfig());

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
        TemperatureConfig config = ScorchfulConfig.getTemperatureConfig();

        int tickInterval = config.getEnvironmentHeatingTickInterval();
        if (tickInterval > 1 && player.tickCount % tickInterval != 0) {
            return TriState.FALSE;
        }

        if (!config.isEnableEnvironmentHeating()) {
            return TriState.FALSE;
        } else {
            return TriState.from(player.thermoo$getTemperatureScale() < config.getMaxEnvironmentHeatingScale());
        }
    }

    static int environmentTemperatureToTemperatureChange(TemperatureRecord temperature, TemperatureConfig config) {
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