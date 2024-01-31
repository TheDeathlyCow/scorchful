package com.github.thedeathlycow.scorchful.enchantment;

import com.github.thedeathlycow.scorchful.registry.SEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SEnchantmentHelper {

    public static int getRehydrationLevel(ItemStack stack) {
        return EnchantmentHelper.getLevel(SEnchantments.REHYDRATION, stack);
    }

    public static int getTotalRehydrationForPlayer(PlayerEntity player) {
        int sum = 0;
        for (ItemStack stack : player.getArmorItems()) {
            sum += getRehydrationLevel(stack);
        }
        return sum;
    }

    private SEnchantmentHelper() {

    }
}
