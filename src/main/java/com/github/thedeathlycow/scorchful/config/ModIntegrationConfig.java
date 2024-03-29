package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".modIntegrationConfig")
public class ModIntegrationConfig implements ConfigData {

    /**
     * Requires Dehydration
     * <p>
     * A direct equivalent would be set this to 8/30:
     * 4 dehydration points reduces water level by 1, and
     * water level is out of 20 * 4 = 80, while waterDrunk is out of 300, so 1 water level is 2/30 of waterDrunk
     * <p>
     * However, due to increased effects of water provided by Dehydration - this drains too fast. So, it is set to a
     * quarter of the direct equivalent draining speed.
     */
    float dehydrationConsumedBySweat = 1f / 30f;

    /**
     * Requires Dehydration
     * <p>
     * Don't lose water to sweat when below this level.
     */
    int minWaterLevelForSweat = 16;

    /**
     * Requires Dehydration
     */
    int maxThirstAddedByRehydration = 4;

    /**
     * Requires Dehydration
     */
    int minWaterLevelToRehydrate = 10;

    public float getDehydrationConsumedBySweat() {
        return dehydrationConsumedBySweat;
    }

    public int getMinWaterLevelForSweat() {
        return minWaterLevelForSweat;
    }

    public int getMaxThirstAddedByRehydration() {
        return maxThirstAddedByRehydration;
    }

    public int getMinWaterLevelToRehydrate() {
        return minWaterLevelToRehydrate;
    }
}
