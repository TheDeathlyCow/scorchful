package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.config.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.dehydration.access.ThirstManagerAccess;
import net.dehydration.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class DehydrationServerThirstPlugin implements ServerThirstPlugin {
    @Override
    public boolean dehydrateFromSweating(PlayerEntity player) {
        DehydrationConfig config = ScorchfulConfig.getDehydrationConfig();
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();
        if (thirstManager.getThirstLevel() > config.getMinWaterLevelForSweat()
                && player.thermoo$getTemperature() > 0) {
            thirstManager.addDehydration(config.getDehydrationConsumedBySweat());
            return true;
        }

        return false;
    }

    @Override
    public void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency) {
        ThirstManager thirstManager = ((ThirstManagerAccess) player).getThirstManager();

        DehydrationConfig dehydrationConfig = ScorchfulConfig.getDehydrationConfig();
        // dont drink if dont have to - prevents rehydration spam
        if (thirstManager.getThirstLevel() > dehydrationConfig.getMinWaterLevelForSweat()) {
            return;
        }

        int maxWater = MathHelper.floor(rehydrationEfficiency * dehydrationConfig.getMaxWaterLost());
        int waterToAdd = player.getRandom().nextBetween(1, maxWater);
        thirstManager.add(waterToAdd);
    }

    @Override
    public int getRehydrationThreshold() {
        return ScorchfulConfig.getDehydrationConfig().getRehydrationDrinkSize();
    }
}