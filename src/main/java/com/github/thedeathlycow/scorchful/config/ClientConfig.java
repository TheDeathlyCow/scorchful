package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = Scorchful.MODID + ".client_config")
public class ClientConfig implements ConfigData {

    boolean doBurningHeartOverlay = true;

    public boolean doBurningHeartOverlay() {
        return doBurningHeartOverlay;
    }
}
