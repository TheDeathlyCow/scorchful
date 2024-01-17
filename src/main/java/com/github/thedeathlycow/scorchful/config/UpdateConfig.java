package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Scorchful.MODID + ".update_config")
public class UpdateConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    int currentConfigVersion = Scorchful.CONFIG_VERSION;

    @ConfigEntry.Gui.Tooltip(count = 3)
    boolean enableConfigUpdates = true;

    public boolean isConfigUpdatesEnabled() {
        return enableConfigUpdates;
    }

}
