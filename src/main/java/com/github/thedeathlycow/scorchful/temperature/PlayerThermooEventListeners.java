package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.event.InitialTemperatureChangeResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class PlayerThermooEventListeners {
    public void applyPassiveHeating(
            EnvironmentController controller,
            PlayerEntity player,
            Biome biome,
            InitialTemperatureChangeResult result
    ) {

        // dont freeze (much) beyond 0, but still allow freezing if warm,
        // and always allow passive heating
        final int changeAmount = result.getInitialChange();
        if (changeAmount < 0 && player.thermoo$isCold()) {
            return;
        }

        final boolean doPassiveHeating = true;

        final boolean canApplyChange = (changeAmount < 0 && player.thermoo$canFreeze())
                || (changeAmount > 0 && player.thermoo$canOverheat() && player.thermoo$getTemperatureScale() <= 1.0);

        if (doPassiveHeating && canApplyChange) {
            float modifier = 0f;
            if (result.getInitialChange() < 0) {
                modifier = getWetnessCoolingModifier(player);
            }

            int temperatureChange = MathHelper.ceil(result.getInitialChange() * (1 + modifier));

            result.setInitialChange(temperatureChange);
            result.applyInitialChange();
        }
    }

    private static float getWetnessCoolingModifier(Soakable soakable) {
        if (soakable.thermoo$ignoresFrigidWater()) {
            return 0.0f;
        }
        return /* replace with config! */ 0.2f * soakable.thermoo$getSoakedScale();
    }

}
