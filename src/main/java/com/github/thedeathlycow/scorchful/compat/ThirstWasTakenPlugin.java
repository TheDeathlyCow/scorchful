package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.config.ThirstWasTakenConfig;
import dev.ghen.thirst.content.thirst.PlayerThirst;
import dev.ghen.thirst.foundation.common.capability.ModAttachment;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ThirstWasTakenPlugin implements ServerThirstPlugin {
    @Override
    public boolean dehydrateFromSweating(Player player) {
        ThirstWasTakenConfig config = Scorchful.getConfig().integrationConfig.thirstWasTakenConfig;
        PlayerThirst thirst = player.getData(ModAttachment.PLAYER_THIRST);

        if (thirst.getThirst() > config.getMinWaterLevelForSweat()
                && player.thermoo$getTemperature() > 0) {
            thirst.addExhaustion(player, config.getDehydrationConsumedBySweat());
            return true;
        }

        return false;
    }

    @Override
    public void rehydrateFromEnchantment(Player player, int waterCaptured, double rehydrationEfficiency) {
        PlayerThirst thirst = player.getData(ModAttachment.PLAYER_THIRST);

        ThirstWasTakenConfig thirstWasTakenConfig = Scorchful.getConfig().integrationConfig.thirstWasTakenConfig;

        int maxWater = Mth.floor(rehydrationEfficiency * thirstWasTakenConfig.getMaxWaterLost());
        int waterToAdd = player.getRandom().nextIntBetweenInclusive(1, maxWater);
        thirst.drink(waterToAdd, waterToAdd);
    }

    @Override
    public int getRehydrationThreshold() {
        return Scorchful.getConfig().integrationConfig.thirstWasTakenConfig.getRehydrationDrinkSize();
    }
}