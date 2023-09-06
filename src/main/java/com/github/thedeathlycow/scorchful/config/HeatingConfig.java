package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    int maxTemperaturePerTick = 3;

    int coolingFromIce = 12;

    int getMinSkyLightLevelForHeat = 13;

    int heatPerSkylightLevel = 2;

    float minTempForScorching = 1f;

    int netherWarmRate = 3;

    public int getMaxTemperaturePerTick() {
        return maxTemperaturePerTick;
    }

    public int getCoolingFromIce() {
        return coolingFromIce;
    }

    public int getGetMinSkyLightLevelForHeat() {
        return getMinSkyLightLevelForHeat;
    }

    public int getHeatPerSkylightLevel() {
        return heatPerSkylightLevel;
    }

    public float getMinTempForScorching() {
        return minTempForScorching;
    }

    public int getNetherWarmRate() {
        return netherWarmRate;
    }
}
