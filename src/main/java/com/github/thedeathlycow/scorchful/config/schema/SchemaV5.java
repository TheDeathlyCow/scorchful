package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;

import java.nio.file.Path;

public class SchemaV5 {
    public static Path getCombatConfigPath() {
        return Scorchful.getConfigDir().resolve("combat.json5");
    }

    public static Path getThirstConfigPath() {
        return Scorchful.getConfigDir().resolve("thirst.json5");
    }
}