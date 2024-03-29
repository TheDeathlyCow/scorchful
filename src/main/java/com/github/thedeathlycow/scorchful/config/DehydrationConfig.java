package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

/**
 * Config for changes to thirst system when using Dehydration
 */
@Config(name = Scorchful.MODID + ".dehydrationConfig")
public class DehydrationConfig implements ConfigData {

    /**
     * A direct equivalent would be set this to 8/30:
     * 4 dehydration points reduces water level by 1, and
     * water level is out of 20 * 4 = 80, while waterDrunk is out of 300, so 1 water level is 2/30 of waterDrunk
     * <p>
     * However, due to increased effects of water provided by Dehydration - this drains too fast. So, it is set to a
     * quarter of the direct equivalent draining speed.
     */
    float dehydrationConsumedBySweat = 1f / 30f;

    /**
     * Don't lose water to sweat when below this level.
     */
    int minWaterLevelForSweat = 16;

    int rehydrationWaterAddedPerLevel = 1;

    int rehydrationDrinkSize = 300;

    int temperatureFromWetness = -5;

    public float getDehydrationConsumedBySweat() {
        return dehydrationConsumedBySweat;
    }

    public int getMinWaterLevelForSweat() {
        return minWaterLevelForSweat;
    }

    public int getRehydrationWaterAddedPerLevel() {
        return rehydrationWaterAddedPerLevel;
    }

    public int getRehydrationDrinkSize() {
        return rehydrationDrinkSize;
    }

    public int getTemperatureFromWetness() {
        return temperatureFromWetness;
    }
}
