package com.github.thedeathlycow.scorchful.compat;

import net.dehydration.api.DehydrationAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ScorchfulDehydration implements DehydrationAPI {
    @Override
    public int calculateDrinkThirst(ItemStack itemStack, PlayerEntity playerEntity) {
        return 0;
    }
}
