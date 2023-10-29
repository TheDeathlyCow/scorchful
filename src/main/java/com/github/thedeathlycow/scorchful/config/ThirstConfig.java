package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".thirst_config")
public class ThirstConfig implements ConfigData {

    int wetnessFromBodyWater = 1;

    int temperatureFromWetness = -3;

    int waterFromQuenchingFood = 50;

    int waterFromDrinking = 250;

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

    public int getWaterFromQuenchingFood() {
        return waterFromQuenchingFood;
    }

    public int getWaterFromDrinking() {
        return waterFromDrinking;
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
