package com.github.thedeathlycow.scorchful.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ThirstPlugin {
    void onConsumeDrink(PlayerEntity player, ItemStack stack);

    void tickSweat(PlayerEntity player);

    int getRehydrationEnchantmentDrinkSize();

    boolean rehydrateFromEnchantment(double rehydrationEfficiency);
}