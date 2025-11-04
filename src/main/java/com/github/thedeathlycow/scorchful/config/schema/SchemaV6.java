package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SchemaV6 {
    public static Path getOldWeatherPath() {
        return Scorchful.getConfigDir().resolve("weather.json5");
    }

    public static Path getNewWeatherPath() {
        return Scorchful.getConfigDir().resolve("common").resolve("weather.json5");
    }

    public static void run() throws IOException {
        Files.move(getOldWeatherPath(), getNewWeatherPath());
    }

    private SchemaV6() {

    }
}