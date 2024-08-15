package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".seasonsConfig")
public class SeasonsConfig implements ConfigData {

    boolean enableSeasonsIntegration = true;

    float wetSeasonHumidBiomeSweatEfficiency = 1f / 6f;

    float drySeasonHumidBiomeSweatEfficiency = 0.5f;

    public boolean enableSeasonsIntegration() {
        return enableSeasonsIntegration;
    }

    public float getWetSeasonHumidBiomeSweatEfficiency() {
        return wetSeasonHumidBiomeSweatEfficiency;
    }

    public float getDrySeasonHumidBiomeSweatEfficiency() {
        return drySeasonHumidBiomeSweatEfficiency;
    }
}
