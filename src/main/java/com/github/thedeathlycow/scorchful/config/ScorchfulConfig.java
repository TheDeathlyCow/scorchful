package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.config.section.*;

public final class ScorchfulConfig {
    public static final String MAIN_CATEGORY_NAME = "main";

    public static ItemConfig getItemConfig() {
        return ItemConfig.HANDLER.instance();
    }

    public static CombatConfig getCombatConfig() {
        return CombatConfig.HANDLER.instance();
    }

    public static WeatherConfig getWeatherConfig() {
        return WeatherConfig.HANDLER.instance();
    }

    public static TemperatureConfig getTemperatureConfig() {
        return TemperatureConfig.HANDLER.instance();
    }

    public static ThirstConfig getThirstConfig() {
        return ThirstConfig.HANDLER.instance();
    }

    public static DehydrationConfig getDehydrationConfig() {
        return DehydrationConfig.HANDLER.instance();
    }

    public static void initialize() {
        Updater.run();

        CombatConfig.HANDLER.load();
        CombatConfig.HANDLER.save();

        DehydrationConfig.HANDLER.load();
        DehydrationConfig.HANDLER.save();

        ItemConfig.HANDLER.load();
        ItemConfig.HANDLER.save();

        TemperatureConfig.HANDLER.load();
        TemperatureConfig.HANDLER.save();

        ThirstConfig.HANDLER.load();
        ThirstConfig.HANDLER.save();

        WeatherConfig.HANDLER.load();
        WeatherConfig.HANDLER.save();
    }

    private ScorchfulConfig() {

    }
}
