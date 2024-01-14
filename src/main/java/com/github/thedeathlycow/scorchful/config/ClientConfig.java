package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".client_config")
public class ClientConfig implements ConfigData {

    boolean doBurningHeartOverlay = true;

    boolean doSunHatShading = true;

    float sunHatShadeOpacity = 0.2f;

    public boolean isSunHatShading() {
        return doSunHatShading;
    }

    public float getSunHatShadeOpacity() {
        return sunHatShadeOpacity;
    }

    public boolean doBurningHeartOverlay() {
        return doBurningHeartOverlay;
    }
}
