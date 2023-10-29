package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    int coolingFromIce = 12;

    int getMinSkyLightLevelForHeat = 13;

    int heatFromSun = 1;

    int scorchingBiomeHeatIncrease = 2;

    int onFireWarmRate = 4;

    public int getCoolingFromIce() {
        return coolingFromIce;
    }

    public int getGetMinSkyLightLevelForHeat() {
        return getMinSkyLightLevelForHeat;
    }

    public int getHeatFromSun() {
        return heatFromSun;
    }

    public int getScorchingBiomeHeatIncrease() {
        return scorchingBiomeHeatIncrease;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }



}
