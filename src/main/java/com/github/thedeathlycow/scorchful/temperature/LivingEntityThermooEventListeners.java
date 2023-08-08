package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialSoakChangeResult;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialTemperatureChangeResult;
import net.minecraft.entity.LivingEntity;

public class LivingEntityThermooEventListeners {

    public void tickHeatEffects(
            EnvironmentController controller,
            LivingEntity entity,
            InitialTemperatureChangeResult result
    ) {
        // dont freeze (much) beyond 0, but still allow freezing if warm,
        // and always allow passive heating
        if (result.getInitialChange() < 0 && entity.thermoo$isCold()) {
            return;
        }

        // applied initial change
        result.applyInitialChange();
    }

    public void tickWetChange(
            EnvironmentController controller,
            LivingEntity entity,
            InitialSoakChangeResult result
    ) {
        // applies initial change
        result.applyInitialChange();
    }

}
