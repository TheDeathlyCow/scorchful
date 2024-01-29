package com.github.thedeathlycow.scorchful.enchantment;

import com.github.thedeathlycow.scorchful.registry.SEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SEnchantmentHelper {

    public static int getRehydrationLevel(ItemStack stack) {
        return EnchantmentHelper.getLevel(SEnchantments.REHYDRATION, stack);
    }

    /**
     * Gets the total {@linkplain SEnchantments#REHYDRATION rehydration} points for the player. The player gets 1 point
     * for each piece of armour enchanted with Rehydration, and 1 point per level of Rehydration on that equipment.
     * <p>
     * For example if the player has Rehydration II on their helmet and Rehydration I on their chest plate, then they get
     * 2 points from the helmet and chest plate, and 3 from the summed levels of the enchantments for a total of 5 points.
     *
     * @param player The player to get rehydration for
     * @return Returns the total number of rehydration points. Returns 0 if they have no rehydration.
     */
    public static int getTotalRehydrationForPlayer(PlayerEntity player) {
        int sum = 0;
        for (ItemStack stack : player.getArmorItems()) {
            int rehydrationLevel = getRehydrationLevel(stack);

            if (rehydrationLevel > 0) {
                sum += rehydrationLevel + 1;
            }
        }
        return sum;
    }
}
