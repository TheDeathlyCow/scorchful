package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Config(name = Scorchful.MODID)
public class ScorchfulConfig extends PartitioningSerializer.GlobalData {

    @Environment(EnvType.CLIENT)
    @ConfigEntry.Gui.CollapsibleObject
    public ClientConfig clientConfig = new ClientConfig();


    @ConfigEntry.Gui.CollapsibleObject
    public EnvironmentConfig environmentConfig = new EnvironmentConfig();

}
