package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Scorchful.MODID)
public class ScorchfulConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.CollapsibleObject
    public ClientConfig clientConfig = new ClientConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public HeatingConfig heatingConfig = new HeatingConfig();
}
