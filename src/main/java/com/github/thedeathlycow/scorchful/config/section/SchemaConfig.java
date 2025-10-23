package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class SchemaConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("schema.json5");

    public static final ConfigClassHandler<SchemaConfig> HANDLER = ConfigClassHandler.createBuilder(SchemaConfig.class)
            .id(Scorchful.id("schema"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    public static final int CONFIG_VERSION = 2;

    @SerialEntry(comment = "Config schema version, do not touch! Changing this value may result in unexpected behaviour.")
    int schemaVersion = CONFIG_VERSION;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}