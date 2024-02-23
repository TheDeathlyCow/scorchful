package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".heating_config")
public class HeatingConfig implements ConfigData {

    boolean enableTurtleArmorEffects = true;

    int coolingFromIce = 24;

    int minSkyLightLevelForHeat = 13;

    int heatFromSun = 2;

    int scorchingBiomeHeatIncrease = 2;

    int sunHatShadeTemperatureChange = -1;

    int onFireWarmRate = 48;

    int fireballHeat = 2000;

    double defaultArmorHeatResistance = -0.5;

    double veryHarmfulArmorHeatResistance = -1.0;

    double protectiveArmorHeatResistance = 0.5;

    double veryProtectiveArmorHeatResistance = 1.0;

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
}
