package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.config.section.*;

public final class ScorchfulConfig {
    public static final String MAIN_CATEGORY_NAME = "main";

    public static ItemConfig getItemConfig() {
        return ItemConfig.HANDLER.instance();
    }

    public static EntityConfig getEntityConfig() {
        return EntityConfig.HANDLER.instance();
    }

    public static WeatherConfig getWeatherConfig() {
        return WeatherConfig.HANDLER.instance();
    }

    public static TemperatureConfig getTemperatureConfig() {
        return TemperatureConfig.HANDLER.instance();
    }

    public static DehydrationConfig getDehydrationConfig() {
        return DehydrationConfig.HANDLER.instance();
    }

    public static void initialize() {
        Updater.run();

        DehydrationConfig.HANDLER.load();
        DehydrationConfig.HANDLER.save();

        EntityConfig.HANDLER.load();
        EntityConfig.HANDLER.save();

        ItemConfig.HANDLER.load();
        ItemConfig.HANDLER.save();

        TemperatureConfig.HANDLER.load();
        TemperatureConfig.HANDLER.save();

        WeatherConfig.HANDLER.load();
        WeatherConfig.HANDLER.save();
    }

    private ScorchfulConfig() {

    }
}
