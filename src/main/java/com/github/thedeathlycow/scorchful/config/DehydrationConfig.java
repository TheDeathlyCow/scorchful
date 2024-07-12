package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
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
        return pointRange / PlayerWaterComponent.MAX_WATER;
    }

    public int getMinWaterLevelForSweat() {
        return minWaterLevelForSweat;
    }

    public int getMaxWaterLost() {
        return 20 - minWaterLevelForSweat;
    }

    public int getRehydrationDrinkSize() {
        return PlayerWaterComponent.MAX_WATER;
    }
}
