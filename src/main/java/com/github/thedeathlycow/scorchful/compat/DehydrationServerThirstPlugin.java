package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class DehydrationServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public boolean dehydrateFromSweating(Player player) {
        DehydrationConfig config = Scorchful.getConfig().integrationConfig.dehydrationConfig;
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();
        if (thirstManager.getThirstLevel() > config.getMinWaterLevelForSweat()
                && player.thermoo$getTemperature() > 0) {
            thirstManager.addDehydration(config.getDehydrationConsumedBySweat());
            return true;
        }

        return false;
    }

    @Override
    public void rehydrateFromEnchantment(Player player, int waterCaptured, double rehydrationEfficiency) {
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();

        DehydrationConfig dehydrationConfig = Scorchful.getConfig().integrationConfig.dehydrationConfig;
        // dont drink if dont have to - prevents rehydration spam
        if (thirstManager.getThirstLevel() > dehydrationConfig.getMinWaterLevelForSweat()) {
            return;
        }

        int maxWater = Mth.floor(rehydrationEfficiency * dehydrationConfig.getMaxWaterLost());
        int waterToAdd = player.getRandom().nextIntBetweenInclusive(1, maxWater);
        thirstManager.add(waterToAdd);
    }

    @Override
    public int getRehydrationThreshold() {
        return Scorchful.getConfig().integrationConfig.dehydrationConfig.getRehydrationDrinkSize();
    }
}