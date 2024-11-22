package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class DehydrationServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public void onConsumeDrink(ServerPlayerEntity player, ItemStack stack) {
        // handled by dehydration mod
    }

    @Override
    public int tickSweatTransfer(PlayerEntity player) {
        return 0;
    }

    @Override
    public void rehydrateFromEnchantment(double rehydrationEfficiency) {
        return;
    }

    @Override
    public int getMaxRehydrationWaterCapacity() {
        return Scorchful.getConfig().getRehydrationDrinkSize(true);
    }
}