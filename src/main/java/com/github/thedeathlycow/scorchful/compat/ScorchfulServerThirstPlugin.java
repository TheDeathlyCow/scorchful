package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.attachment.PlayerWaterAttachment;
import com.github.thedeathlycow.scorchful.attachment.RehydrationAttachment;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * The default behaviour for Scorchful's thirst interactions
 */
public final class ScorchfulServerThirstPlugin implements ServerThirstPlugin {
    /**
     * Attempts to dehydrate the player from sweating.
     *
     * @param player The player to dehydrate
     * @return Returns {@code true} if the player was successfully dehydrated and can have that water added to their
     * {@linkplain com.github.thedeathlycow.thermoo.api.temperature.Soakable soaked ticks}.
     */
    @Override
    public boolean dehydrateFromSweating(Player player) {
        PlayerWaterAttachment waterComponent = player.getData(ScorchfulEntityAttachments.PLAYER_WATER);
        if (waterComponent.getWaterDrunk() > 0 && player.thermoo$getTemperature() > 0) {
            waterComponent.drink(-1);
            return true;
        }

        return false;
    }

    /**
     * Rehydrates the player from {@linkplain RehydrationAttachment Rehydration}.
     * <p>
     * Rehydration is usually provided as an {@link net.minecraft.world.item.enchantment.Enchantment}, but is internally based on an
     * {@linkplain com.github.thedeathlycow.scorchful.registry.SEntityAttributes#REHYDRATION_EFFICIENCY attribute}.
     *
     * @param player                The player to rehydrate
     * @param waterCaptured         The water that Rehydration has captured, as {@linkplain com.github.thedeathlycow.thermoo.api.temperature.Soakable soaked ticks}
     * @param rehydrationEfficiency How much of the water recaptured should be converted to thirst water, as a percentage
     *                              between 0 and 1.
     */
    @Override
    public void rehydrateFromEnchantment(Player player, int waterCaptured, double rehydrationEfficiency) {
        // don't drink if we already have water (and dont need to) - prevents rehydration spam
        PlayerWaterAttachment waterComponent = player.getData(ScorchfulEntityAttachments.PLAYER_WATER);
        if (waterComponent.getWaterDrunk() > 1) {
            return;
        }

        ThirstConfig config = Scorchful.getConfig().thirstConfig;
        double efficiency = config.getMaxRehydrationEfficiency() * rehydrationEfficiency;
        int drinkToAdd = Mth.floor(waterCaptured * efficiency);

        if (drinkToAdd > 0) {
            waterComponent.drink(drinkToAdd);
        }
    }
}