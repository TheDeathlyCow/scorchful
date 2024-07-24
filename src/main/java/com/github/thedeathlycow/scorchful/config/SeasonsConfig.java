package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".seasonsConfig")
public class SeasonsConfig implements ConfigData {

    boolean enableSeasonsIntegration = true;

    public boolean enableSeasonsIntegration() {
        return enableSeasonsIntegration;
    }

}
