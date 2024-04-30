package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    boolean doPassiveHeating = true;

    float maxPassiveHeatingScale = 1.0f;

    boolean enableTurtleArmorEffects = true;

    int coolingFromIce = 12;

    int minSkyLightLevelForHeat = 13;

    int heatFromSun = 1;

    int scorchingBiomeHeatIncrease = 1;

    int sunHatShadeTemperatureChange = -1;

    int onFireWarmRate = 24;

    int onFireWarmRateWithFireResistance = 6;

    int inLavaWarmRate = 24;

    int striderOutOfLavaCoolRate = 24;

    int powderSnowCoolRate = 24;

    int fireballHeat = 1000;

    double defaultArmorHeatResistance = -0.5;

    double veryHarmfulArmorHeatResistance = -1.0;

    double protectiveArmorHeatResistance = 0.5;

    double veryProtectiveArmorHeatResistance = 1.0;

    int waterBreathingDurationPerTurtleArmorPieceSeconds = 10;

    int lightLevelPerHeatInNether = 4;

    int minLightLevelForHeatInNether = 11;

    int blocksAboveLavaOceanPerHeatInNether = 5;

    int maxHeatFromLavaOceanInNether = 3;

    public boolean doPassiveHeating() {
        return doPassiveHeating;
    }

    public float getMaxPassiveHeatingScale() {
        return maxPassiveHeatingScale;
    }

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public int getCoolingFromIce() {
        return coolingFromIce;
    }

    public int getMinSkyLightLevelForHeat() {
        return minSkyLightLevelForHeat;
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

    public double getDefaultArmorHeatResistance() {
        return defaultArmorHeatResistance;
    }

    public double getVeryHarmfulArmorHeatResistance() {
        return veryHarmfulArmorHeatResistance;
    }

    public double getProtectiveArmorHeatResistance() {
        return protectiveArmorHeatResistance;
    }

    public double getVeryProtectiveArmorHeatResistance() {
        return veryProtectiveArmorHeatResistance;
    }

    public int getWaterBreathingDurationPerTurtleArmorPieceSeconds() {
        return waterBreathingDurationPerTurtleArmorPieceSeconds;
    }

    public int getLightLevelPerHeatInNether() {
        return lightLevelPerHeatInNether;
    }

    public int getMinLightLevelForHeatInNether() {
        return minLightLevelForHeatInNether;
    }

    public int getBlocksAboveLavaOceanPerHeatInNether() {
        return blocksAboveLavaOceanPerHeatInNether;
    }

    public int getMaxHeatFromLavaOceanInNether() {
        return maxHeatFromLavaOceanInNether;
    }
}
