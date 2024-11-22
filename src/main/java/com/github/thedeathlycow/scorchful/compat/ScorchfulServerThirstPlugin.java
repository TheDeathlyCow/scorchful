package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class ScorchfulServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public void onConsumeDrink(ServerPlayerEntity player, ItemStack stack) {
        DrinkLevelComponent drink = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (drink == null) {
            return;
        }

        PlayerWaterComponent component = ScorchfulComponents.PLAYER_WATER.get(player);

        int water = drink.getDrinkingWater(Scorchful.getConfig().thirstConfig);
        component.drink(water);

        if (component.getWaterDrunk() >= PlayerWaterComponent.MAX_WATER * 0.9) {
            player.playSound(SSoundEvents.ENTITY_GULP, 1f, 1f);
        }
    }

    @Override
    public boolean transferWaterToSweat(PlayerEntity player) {
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