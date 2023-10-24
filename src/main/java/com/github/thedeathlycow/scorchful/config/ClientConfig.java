package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".client_config")
public class ClientConfig implements ConfigData {

    boolean doBurningHeartOverlay = true;

    public boolean doBurningHeartOverlay() {
        return doBurningHeartOverlay;
    }
}
