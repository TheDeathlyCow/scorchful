package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
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

            Scorchful.LOGGER.warn("A few options in Scorchful's Temperature Settings config were reset by Schema V7 to fix a bug in the auto update script. If you have modified the config, you may need to manually re-adjust these settings.");
        }
    }

    private SchemaV7() {

    }
}