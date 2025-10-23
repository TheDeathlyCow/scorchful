package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.schema.ConfigUpdater;
import com.github.thedeathlycow.scorchful.config.schema.SchemaV2;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class Updater {
    private static final Int2ObjectMap<ConfigUpdater> SCHEMAS = Util.make(
            new Int2ObjectArrayMap<>(),
            map -> {
                map.put(2, SchemaV2::run);
            }
    );

    static void run() {
        Path clothConfigPath = FabricLoader.getInstance().getConfigDir().resolve("scorchful.json");
        if (Files.exists(clothConfigPath)) {
            try {
                updateToYACL(clothConfigPath);
                Scorchful.LOGGER.info("Scorchful config files successfully updated to YACL format");
            } catch (Exception e) {
                Scorchful.LOGGER.error("Unable to update config file to YACL", e);
            }
        }

        SchemaConfig.HANDLER.load();

        final int latestSchemaVersion = SchemaConfig.CONFIG_VERSION;
        final int currentSchemaVersion = SchemaConfig.HANDLER.instance().getSchemaVersion();

        for (int step = currentSchemaVersion; step < latestSchemaVersion; step++) {
            ConfigUpdater updater = SCHEMAS.get(step);
            if (updater != null) {
                try {
                    updater.run();
                } catch (IOException e) {
                    Scorchful.LOGGER.warn("Unable to upgrade config file from schema version {} to {}, due to IO exception. Aborting upgrade.", step - 1, step, e);
                    break;
                }
            }
        }
    }

    private static void updateToYACL(Path oldConfigPath) throws IOException {
        Scorchful.LOGGER.info("Attempting to update Scorchful config files to YACL");

        String content = Files.readString(oldConfigPath);

        JsonObject root = JsonParser.parseString(content).getAsJsonObject();
        JsonObject clientConfig = root.remove("clientConfig").getAsJsonObject();
        JsonObject heatingConfig = root.remove("heatingConfig").getAsJsonObject();
        JsonObject combatConfig = root.remove("combatConfig").getAsJsonObject();
        JsonObject weatherConfig = root.remove("weatherConfig").getAsJsonObject();
        JsonObject thirstConfig = root.remove("thirstConfig").getAsJsonObject();
        JsonObject dehydrationConfig = root.getAsJsonObject("integrationConfig").remove("dehydrationConfig").getAsJsonObject();

        boolean writeSchemaFile = copyOldConfigObject(clientConfig, SchemaV2.getOldClientConfigPath());
        writeSchemaFile &= copyOldConfigObject(combatConfig, CombatConfig.PATH);
        writeSchemaFile &= copyOldConfigObject(heatingConfig, HeatingConfig.PATH);
        writeSchemaFile &= copyOldConfigObject(weatherConfig, WeatherConfig.PATH);
        writeSchemaFile &= copyOldConfigObject(thirstConfig, ThirstConfig.PATH);
        writeSchemaFile &= copyOldConfigObject(dehydrationConfig, DehydrationConfig.PATH);

        if (writeSchemaFile) {
            JsonObject json = new JsonObject();
            json.addProperty("schemaVersion", 1);
            Files.writeString(SchemaConfig.PATH, json.toString(), StandardOpenOption.CREATE);
        }

        Files.delete(oldConfigPath);
    }

    private static boolean copyOldConfigObject(JsonObject json, Path dest) throws IOException {
        if (!Files.exists(dest)) {
            Files.createDirectories(dest.getParent());
            Files.writeString(dest, json.toString(), StandardOpenOption.CREATE);
            return true;
        } else {
            Scorchful.LOGGER.warn("Config file {} already exists, skipping upgrade", dest);
            return false;
        }
    }

    private Updater() {

    }
}