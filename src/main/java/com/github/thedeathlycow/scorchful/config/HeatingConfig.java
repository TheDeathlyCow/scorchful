package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    boolean doPassiveHeating = true;

    int passiveHeatingTickInterval = 1;

    float maxPassiveHeatingScale = 1.0f;

    boolean enableTurtleArmorEffects = true;

    @ConfigEntry.Gui.Tooltip
    double minTemperatureForHeatC = 30.0;

    @ConfigEntry.Gui.Tooltip
    double degreesCPerTemperatureIncrease = 10.0;

    double environmentTemperatureMultiplier = 1.0;

    int coolingFromIce = 12;

    int onFireWarmRate = 24;

    int onFireWarmRateWithFireResistance = 6;

    int inLavaWarmRate = 24;

    int striderOutOfLavaCoolRate = 24;

    int powderSnowCoolRate = 24;

    int fireballHeat = 1000;

    float turtleArmorLungCapacityMultiplier = 1.0f;

    int temperatureFromCoolingFood = -1260;

    public boolean doPassiveHeating() {
        return doPassiveHeating;
    }

    public int getPassiveHeatingTickInterval() {
        return passiveHeatingTickInterval;
    }

    public float getMaxPassiveHeatingScale() {
        return maxPassiveHeatingScale;
    }

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public double getMinTemperatureForHeatC() {
        return minTemperatureForHeatC;
    }

    public double getDegreesCPerTemperatureIncrease() {
        return degreesCPerTemperatureIncrease;
    }

    public double getEnvironmentTemperatureMultiplier() {
        return environmentTemperatureMultiplier;
    }

    public int getCoolingFromIce() {
        return coolingFromIce;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }

    public int getOnFireWarmRateWithFireResistance() {
        return onFireWarmRateWithFireResistance;
    }

    public int getInLavaWarmRate() {
        return inLavaWarmRate;
    }

    public int getStriderOutOfLavaCoolRate() {
        return striderOutOfLavaCoolRate;
    }

    public int getPowderSnowCoolRate() {
        return powderSnowCoolRate;
    }

    public int getFireballHeat() {
        return fireballHeat;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }

    public int getTemperatureFromCoolingFood() {
        return temperatureFromCoolingFood;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();

        if (this.minTemperatureForHeatC < 25) {
            this.minTemperatureForHeatC = 25;
        }

        if (this.degreesCPerTemperatureIncrease <= 0) {
            throw new ValidationException("Degrees C Per Temperature Increase must be positive!");
        }
    }
}
