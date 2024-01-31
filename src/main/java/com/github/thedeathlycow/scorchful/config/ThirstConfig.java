package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".thirst_config")
public class ThirstConfig implements ConfigData {

    int temperatureFromWetness = -12;

    int waterFromRefreshingFood = 30;

    int waterFromSustainingFood = 60;

    int waterFromHydratingFood = 120;

    int rehydrationDrinkSize = 120;

    int soakingFromSplashPotions = 300;

    int rainWetnessIncrease = 1;

    int touchingWaterWetnessIncrease = 5;

    int dryRate = 1;

    int onFireDryDate = 50;

    float humidBiomeSweatEfficiency = 1f / 6f;

    float maxRehydrationEfficiency = 0.75f;

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

    public int getRehydrationDrinkSize() {
        return rehydrationDrinkSize;
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

    public float getHumidBiomeSweatEfficiency() {
        return humidBiomeSweatEfficiency;
    }

    public float getMaxRehydrationEfficiency() {
        return maxRehydrationEfficiency;
    }
}
