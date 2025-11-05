package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.config.section.TemperatureConfig;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Fixes some bugs in the SchemaV2 updater.
 */
public final class SchemaV7 {
    public static Path getTemperatureConfigPath() {
        return TemperatureConfig.PATH;
    }

    public static void run(int originalSchemaVersion) throws IOException {
        if (originalSchemaVersion > 1 && originalSchemaVersion < 7) {
            Path path = getTemperatureConfigPath();
            JsonObject temperatureConfig = JsonCopyHelper.read(path);

            temperatureConfig.addProperty("soakedTemperatureMultiplier", 1.0f);
            temperatureConfig.addProperty("coolingFoodTemperatureMultiplier", 1.0f);

            temperatureConfig.addProperty("humidBiomeSweatEfficiencyMultiplier", 1.0f);
            temperatureConfig.addProperty("extraHumidBiomeSweatEfficiencyMultiplier", 1.0f);
            temperatureConfig.addProperty("aridBiomeSweatEfficiencyMultiplier", 1.0f);

            Files.writeString(getTemperatureConfigPath(), temperatureConfig.toString());
        }
    }

    private SchemaV7() {

    }
}