package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Scorchful.MODID)
public class ScorchfulConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.CollapsibleObject
    public ClientConfig clientConfig = new ClientConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public HeatingConfig heatingConfig = new HeatingConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public ThirstConfig thirstConfig = new ThirstConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public UpdateConfig updateConfig = new UpdateConfig();

    public static void updateConfig(ConfigHolder<ScorchfulConfig> configHolder) {
        UpdateConfig config = configHolder.getConfig().updateConfig;

        if (config.isConfigUpdatesEnabled() && config.currentConfigVersion != Scorchful.CONFIG_VERSION) {
            config.currentConfigVersion = Scorchful.CONFIG_VERSION;
            configHolder.resetToDefault();
            configHolder.save();

            Scorchful.LOGGER.info("The Scorchful Config has been reset due to an update to the default values. " +
                    "You may disable these updates if you don't want this to happen.");
        }
    }
}
