package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Scorchful.MODID + ".integrationConfig")
public class ModIntegrationConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public DehydrationConfig dehydrationConfig = new DehydrationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public SeasonsConfig seasonsConfig = new SeasonsConfig();

}
