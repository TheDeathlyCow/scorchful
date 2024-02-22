package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".weather_config")
public class WeatherConfig implements ConfigData {

    boolean doSandPileAccumulation = true;

    int sandPileAccumulationHeight = 1;

    public boolean isSandPileAccumulationEnabled() {
        return doSandPileAccumulation;
    }

    public int getSandPileAccumulationHeight() {
        return sandPileAccumulationHeight;
    }
}
