package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class SchemaV5 {
    public static Path getCombatConfigPath() {
        return Scorchful.getConfigDir().resolve("combat.json5");
    }

    public static Path getThirstConfigPath() {
        return Scorchful.getConfigDir().resolve("thirst.json5");
    }

    public static void run() throws IOException {
        JsonObject oldCombatConfig = JsonCopyHelper.read(getCombatConfigPath());
        JsonObject oldThirstConfig = JsonCopyHelper.read(getThirstConfigPath());

        var entityConfig = new JsonObject();

        JsonCopyHelper.moveInto(oldCombatConfig, entityConfig);
        JsonCopyHelper.moveInto(oldThirstConfig, entityConfig);

        Path path = Scorchful.getConfigDir().resolve("common").resolve("item.json5");
        Files.writeString(path, entityConfig.toString(), StandardOpenOption.CREATE);

        Files.delete(getCombatConfigPath());
        Files.delete(getThirstConfigPath());
    }

    private SchemaV5() {

    }
}