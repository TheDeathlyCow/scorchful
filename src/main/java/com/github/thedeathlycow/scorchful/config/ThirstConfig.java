package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".thirst_config")
public class ThirstConfig implements ConfigData {

    int wetnessFromBodyWater = 2;

    int temperatureFromWetness = -12;

    int waterFromRefreshingFood = 15;

    int waterFromSustainingFood = 30;

    int waterFromHydratingFood = 60;

    int soakingFromSplashPotions = 300;

    int rainWetnessIncrease = 1;

    int touchingWaterWetnessIncrease = 5;

    int dryRate = 1;

    int onFireDryDate = 50;

    float humidBiomeWetEfficieny = 1f / 6f;

    public int getWetnessFromBodyWater() {
        return wetnessFromBodyWater;
    }

    public int getTemperatureFromWetness() {
        return temperatureFromWetness;
    }

    public int getWaterFromRefreshingFood() {
        return waterFromRefreshingFood;
    }

    public int getWaterFromSustainingFood() {
        return waterFromSustainingFood;
    }

    public int getWaterFromHydratingFood() {
        return waterFromHydratingFood;
    }

    public int getSoakingFromSplashPotions() {
        return soakingFromSplashPotions;
    }

    public int getRainWetnessIncrease() {
        return rainWetnessIncrease;
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getDryRate() {
        return dryRate;
    }

    public int getOnFireDryDate() {
        return onFireDryDate;
    }

    public float getHumidBiomeWetEfficieny() {
        return humidBiomeWetEfficieny;
    }
}
