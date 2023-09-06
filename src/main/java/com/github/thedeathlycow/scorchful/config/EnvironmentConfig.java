package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".environment_config")
public class EnvironmentConfig implements ConfigData {

    boolean doDryBiomeNightFreezing = true;

    double biomeTemperatureMultiplier = 4.0;

    double passiveFreezingCutoffTemp = 0.25;

    double nightTimeTemperatureDecrease = 0.25;

    int ultrawarmWarmRate = 15;

    float dryBiomeNightTemperature = 0.0f;

    public boolean doDryBiomeNightFreezing() {
        return doDryBiomeNightFreezing;
    }

    public double getBiomeTemperatureMultiplier() {
        return biomeTemperatureMultiplier;
    }

    public double getPassiveFreezingCutoffTemp() {
        return passiveFreezingCutoffTemp;
    }

    public double getNightTimeTemperatureDecrease() {
        return nightTimeTemperatureDecrease;
    }

    public int getUltrawarmWarmRate() {
        return ultrawarmWarmRate;
    }

    public float getDryBiomeNightTemperature() {
        return dryBiomeNightTemperature;
    }
}
