package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class ScorchfulServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public boolean dehydrateFromSweating(PlayerEntity player) {
        PlayerWaterComponent waterComponent = ScorchfulComponents.PLAYER_WATER.get(player);
        if (waterComponent.getWaterDrunk() > 0 && player.thermoo$getTemperature() > 0) {
            waterComponent.drink(-1);
            return true;
        }

        return false;
    }

    @Override
    public void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency) {
        // don't drink if we already have water (and dont need to) - prevents rehydration spam
        PlayerWaterComponent waterComponent = ScorchfulComponents.PLAYER_WATER.get(player);
        if (waterComponent.getWaterDrunk() > 1) {
            return;
        }

        ThirstConfig config = Scorchful.getConfig().thirstConfig;
        double efficiency = config.getMaxRehydrationEfficiency() * rehydrationEfficiency;
        int drinkToAdd = MathHelper.floor(waterCaptured * efficiency);

        if (drinkToAdd > 0) {
            waterComponent.drink(drinkToAdd);
        }
    }
}