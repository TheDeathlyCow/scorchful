package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;

import java.nio.file.Path;

public class SchemaV2 {
    public static Path getOldClientConfigPath() {
        return Scorchful.getConfigDir().resolve("client.json5");
    }
}