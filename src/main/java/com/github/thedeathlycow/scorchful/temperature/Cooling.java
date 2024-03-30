package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Cooling {

    public static void tick(LivingEntity entity) {
        if (entity.thermoo$getTemperature() > 0 && entity.thermoo$isWet()) {
            ScorchfulConfig config = Scorchful.getConfig();

            int temperatureChange = config.thirstConfig.getTemperatureFromWetness();
            World world = entity.getWorld();
            if (world.getBiome(entity.getBlockPos()).isIn(SBiomeTags.HUMID_BIOMES)) {
                float efficiency = config.thirstConfig.getHumidBiomeSweatEfficiency();
                temperatureChange = MathHelper.floor(temperatureChange * efficiency);
            }

            entity.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
        }
    }

    private Cooling() {

    }

}
