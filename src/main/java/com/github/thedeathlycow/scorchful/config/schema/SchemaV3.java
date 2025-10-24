package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class SchemaV3 {
    public static Path getHeatingConfigPath() {
        return Scorchful.getConfigDir().resolve("heating.json5");
    }

    public static Path getThirstConfigPath() {
        return Scorchful.getConfigDir().resolve("thirst.json5");
    }

    public static void run() throws IOException {
        JsonObject oldHeatingConfig = JsonCopyHelper.read(getHeatingConfigPath());
        JsonObject oldThirstConfig = JsonCopyHelper.read(getThirstConfigPath());

        var temperatureConfig = new JsonObject();

        // new values
        temperatureConfig.addProperty("coolingMultiplier", 1.0f);
        temperatureConfig.addProperty("heatingMultiplier", 1.0f);
        temperatureConfig.addProperty("sweatEfficiency", 1.0f);

        // heating config update
        JsonCopyHelper.copyBooleanProperty(oldHeatingConfig, temperatureConfig, "doPassiveHeating");
        JsonCopyHelper.rename(temperatureConfig, "doPassiveHeating", "enableEnvironmentHeating");

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "passiveHeatingTickInterval");
        JsonCopyHelper.rename(temperatureConfig, "passiveHeatingTickInterval", "environmentHeatingTickInterval");

        JsonCopyHelper.copyFloatProperty(oldHeatingConfig, temperatureConfig, "maxPassiveHeatingScale");
        JsonCopyHelper.rename(temperatureConfig, "maxPassiveHeatingScale", "maxEnvironmentHeatingScale");

        JsonCopyHelper.copyDoubleProperty(oldHeatingConfig, temperatureConfig, "minTemperatureForHeatC");
        JsonCopyHelper.copyDoubleProperty(oldHeatingConfig, temperatureConfig, "degreesCPerTemperatureIncrease");

        JsonCopyHelper.copyFloatProperty(oldHeatingConfig, temperatureConfig, "environmentTemperatureMultiplier");

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "onFireWarmRate");
        JsonCopyHelper.rename(temperatureConfig, "onFireWarmRate", "burningTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "burningTemperatureMultiplier", 24);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "onFireWarmRateWithFireResistance");
        JsonCopyHelper.rename(temperatureConfig, "onFireWarmRateWithFireResistance", "burningTemperatureMultiplierWithFireResistance");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "burningTemperatureMultiplierWithFireResistance", 6, 0.25f);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "inLavaWarmRate");
        JsonCopyHelper.rename(temperatureConfig, "inLavaWarmRate", "inLavaTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "inLavaTemperatureMultiplier", 24);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "powderSnowCoolRate");
        JsonCopyHelper.rename(temperatureConfig, "powderSnowCoolRate", "powderSnowTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "powderSnowTemperatureMultiplier", 24);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "fireballHeat");
        JsonCopyHelper.rename(temperatureConfig, "fireballHeat", "fireballTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "fireballTemperatureMultiplier", 1000);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "coolingFromIce");
        JsonCopyHelper.rename(temperatureConfig, "coolingFromIce", "iceTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "iceTemperatureMultiplier", 12);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "temperatureFromCoolingFood");
        JsonCopyHelper.rename(temperatureConfig, "temperatureFromCoolingFood", "coolingFoodTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "coolingFoodTemperatureMultiplier", -1260);

        JsonCopyHelper.copyIntProperty(oldHeatingConfig, temperatureConfig, "striderOutOfLavaCoolRate");
        JsonCopyHelper.rename(temperatureConfig, "striderOutOfLavaCoolRate", "striderCoolingTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "striderCoolingTemperatureMultiplier", 24);

        // thirst config update
        JsonCopyHelper.copyIntProperty(oldThirstConfig, temperatureConfig, "temperatureFromWetness");
        JsonCopyHelper.rename(temperatureConfig, "temperatureFromWetness", "soakedTemperatureMultiplier");
        JsonCopyHelper.convertIntToFloatMultiplier(temperatureConfig, "soakedTemperatureMultiplier", -6);

        JsonCopyHelper.copyIntProperty(oldThirstConfig, temperatureConfig, "humidBiomeSweatEfficiency");
        JsonCopyHelper.rename(temperatureConfig, "humidBiomeSweatEfficiency", "humidBiomeSweatEfficiencyMultiplier");
        JsonCopyHelper.normalizeFloat(temperatureConfig, "humidBiomeSweatEfficiencyMultiplier", 1f / 3f);

        JsonCopyHelper.copyIntProperty(oldThirstConfig, temperatureConfig, "extraHumidBiomeSweatEfficiency");
        JsonCopyHelper.rename(temperatureConfig, "extraHumidBiomeSweatEfficiency", "extraHumidBiomeSweatEfficiencyMultiplier");
        JsonCopyHelper.normalizeFloat(temperatureConfig, "extraHumidBiomeSweatEfficiencyMultiplier", 1f / 6f);

        JsonCopyHelper.copyIntProperty(oldThirstConfig, temperatureConfig, "aridBiomeSweatEfficiency");
        JsonCopyHelper.rename(temperatureConfig, "aridBiomeSweatEfficiency", "aridBiomeSweatEfficiencyMultiplier");
        JsonCopyHelper.normalizeFloat(temperatureConfig, "aridBiomeSweatEfficiencyMultiplier", 1.5f);

        // write files
        Path basePath = Scorchful.getConfigDir().resolve("common");
        Files.createDirectories(basePath);

        Path path = basePath.resolve("temperature.json5");
        Files.writeString(path, temperatureConfig.toString(), StandardOpenOption.CREATE);

        Files.writeString(getHeatingConfigPath(), oldHeatingConfig.toString(), StandardOpenOption.CREATE);

        Files.writeString(getThirstConfigPath(), oldThirstConfig.toString(), StandardOpenOption.CREATE);
    }

    private SchemaV3() {

    }
}