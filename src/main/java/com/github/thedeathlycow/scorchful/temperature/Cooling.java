package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Cooling {

    public static void tick(LivingEntity entity) {
        if (entity.thermoo$getTemperature() > 0 && entity.thermoo$isWet()) {
            ThirstConfig config = Scorchful.getConfig().thirstConfig;

            int temperatureChange = config.getTemperatureFromWetness();
            World world = entity.getWorld();
            if (world.getBiome(entity.getBlockPos()).isIn(SBiomeTags.HUMID_BIOMES)) {
                temperatureChange = MathHelper.floor(temperatureChange * config.getHumidBiomeSweatEfficiency());
            }

            entity.thermoo$addTemperature(temperatureChange);
        }
    }

    private Cooling() {

    }

}
