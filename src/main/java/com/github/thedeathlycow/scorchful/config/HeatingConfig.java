package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    int coolingFromIce = 24;

    int getMinSkyLightLevelForHeat = 13;

    int heatFromSun = 2;

    int scorchingBiomeHeatIncrease = 2;

    int sunHatShadeTemperatureChange = -1;

    int onFireWarmRate = 48;

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

    public int getSunHatShadeTemperatureChange() {
        return sunHatShadeTemperatureChange;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }



}
