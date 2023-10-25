package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    int coolingFromIce = 12;

    int getMinSkyLightLevelForHeat = 13;

    int heatFromSun = 1;

    int scorchingBiomeHeatIncrease = 1;

    int onFireWarmRate = 4;

    int wetnessFromBodyWater = 1;

    int temperatureFromWetness = -3;

    int waterFromQuenchingFood = 50;

    int waterFromWaterAndPotions = 250;

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

    public int getWetnessFromBodyWater() {
        return wetnessFromBodyWater;
    }

    public int getTemperatureFromWetness() {
        return temperatureFromWetness;
    }

    public int getWaterFromQuenchingFood() {
        return waterFromQuenchingFood;
    }

    public int getWaterFromPotions() {
        return waterFromWaterAndPotions;
    }
}
