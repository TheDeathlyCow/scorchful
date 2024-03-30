package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.PlayerComponent;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

/**
 * Config for changes to thirst system when using Dehydration
 */
@Config(name = Scorchful.MODID + ".dehydrationConfig")
public class DehydrationConfig implements ConfigData {

    /**
     * Don't lose water to sweat when below this level.
     */
    int minWaterLevelForSweat = 16;

    int maxRehydrationWaterAddedPerLevel = 1;

    /**
     * How this value was derived:
     * <p>
     * Scorchful only takes 4 points of thirst. 4 points of dehydration per thirst so 4 * 4 = 16 dehydration points.
     * <p>
     * There are 300 body water points in Scorchful.
     * <p>
     * 16 dehydration points = 300 body water points. So the conversion factor is 16 dp / 300 bw.
     */
    public float getDehydrationConsumedBySweat() {
        // adapt based on size of bar
        float pointRange = (20f - minWaterLevelForSweat) * 4f;
        return pointRange / PlayerComponent.MAX_WATER;
    }

    public int getMinWaterLevelForSweat() {
        return minWaterLevelForSweat;
    }

    public int getMaxRehydrationWaterAddedPerLevel() {
        return maxRehydrationWaterAddedPerLevel;
    }

    public int getRehydrationDrinkSize() {
        return PlayerComponent.MAX_WATER;
    }
}
