package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.config.section.WeatherConfig;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;

/// Renames an inconsistent config option
public final class SchemaV8 {
    public static void run(int originalSchemaVersion) throws IOException {
        JsonObject weatherConfig = JsonCopyHelper.read(WeatherConfig.PATH);
        JsonCopyHelper.rename(weatherConfig, "doSandPileAccumulation", "enableSandPileAccumulation");
        Files.writeString(WeatherConfig.PATH, weatherConfig.toString());
    }

    private SchemaV8() {

    }
}