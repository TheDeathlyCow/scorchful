package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

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
    public void tickSweat(PlayerEntity player) {

    }

    @Override
    public int getRehydrationEnchantmentDrinkSize() {
        return 0;
    }

    @Override
    public boolean rehydrateFromEnchantment(double rehydrationEfficiency) {
        return false;
    }
}