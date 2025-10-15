package com.github.thedeathlycow.scorchful.config;

public final class ScorchfulConfig {
    public static ClientConfig getClientConfig() {
        return ClientConfig.HANDLER.instance();
    }

    public static HeatingConfig getHeatingConfig() {
        return HeatingConfig.HANDLER.instance();
    }

    public static CombatConfig getCombatConfig() {
        return CombatConfig.HANDLER.instance();
    }

    public static WeatherConfig getWeatherConfig() {
        return WeatherConfig.HANDLER.instance();
    }

    public static ThirstConfig getThirstConfig() {
        return ThirstConfig.HANDLER.instance();
    }

    public static DehydrationConfig getDehydrationConfig() {
        return DehydrationConfig.HANDLER.instance();
    }

    public static void initialize() {
        ClientConfig.HANDLER.load();
        ClientConfig.HANDLER.save();

        CombatConfig.HANDLER.load();
        CombatConfig.HANDLER.save();

        DehydrationConfig.HANDLER.load();
        DehydrationConfig.HANDLER.save();

        HeatingConfig.HANDLER.load();
        HeatingConfig.HANDLER.save();

        ThirstConfig.HANDLER.load();
        ThirstConfig.HANDLER.save();

        WeatherConfig.HANDLER.load();
        WeatherConfig.HANDLER.save();
    }

    private ScorchfulConfig() {

    }
}
