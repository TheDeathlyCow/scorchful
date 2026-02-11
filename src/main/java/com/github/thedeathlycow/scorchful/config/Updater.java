package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.schema.*;
import com.github.thedeathlycow.scorchful.config.section.DehydrationConfig;
import com.github.thedeathlycow.scorchful.config.section.SchemaConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

class Updater {
    private static final Supplier<Int2ObjectMap<ConfigUpdater>> SCHEMAS = () -> {
        Int2ObjectMap<ConfigUpdater> map = new Int2ObjectArrayMap<>();
        map.put(2, SchemaV2::run);
        map.put(3, SchemaV3::run);
        map.put(4, SchemaV4::run);
        map.put(5, SchemaV5::run);
        map.put(6, SchemaV6::run);
        map.put(7, SchemaV7::run);
        return map;
    };

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
        SchemaConfig schemaConfig = SchemaConfig.HANDLER.instance();

        final int latestSchemaVersion = SchemaConfig.CONFIG_VERSION;
        final int currentSchemaVersion = schemaConfig.getSchemaVersion();

        boolean continueUpgrade;

        if (currentSchemaVersion < latestSchemaVersion) {
            Scorchful.LOGGER.info("Scorchful config is out of date! Config files will be automatically upgraded.");
            continueUpgrade = true;
        } else if (currentSchemaVersion > latestSchemaVersion) {
            Scorchful.LOGGER.error(
                    "Current scorchful config schema version {} is greater than the latest supported by " +
                            "this version ({}). This may result in unexpected changes to the config files, are you " +
                            "sure you're using the right mod version?",
                    currentSchemaVersion,
                    latestSchemaVersion
            );
            continueUpgrade = false;
        } else {
            Scorchful.LOGGER.info("Scorchful config is up tp date!");
            continueUpgrade = false;
        }

        if (!continueUpgrade) {
            return;
        }

        Int2ObjectMap<ConfigUpdater> schemas = SCHEMAS.get();

        for (int step = currentSchemaVersion + 1; step <= latestSchemaVersion; step++) {
            ConfigUpdater updater = schemas.get(step);

            if (updater != null) {
                try {
                    updater.run(currentSchemaVersion);
                } catch (IOException e) {
                    Scorchful.LOGGER.warn(
                            "Unable to upgrade config file from schema version {} to {}, due to IO error. Aborting upgrade.",
                            step - 1,
                            step,
                            e
                    );
                    break;
                }
            }

            schemaConfig.setSchemaVersion(step);
        }

        SchemaConfig.HANDLER.save();
        Scorchful.LOGGER.info(
                "Scorchful config successfully updated from schema version {} to {}.",
                currentSchemaVersion,
                latestSchemaVersion
        );
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
        JsonObject dehydrationConfig = root.getAsJsonObject("integrationConfig")
                .remove("dehydrationConfig")
                .getAsJsonObject();

        boolean writeSchemaFile = copyOldConfigObject(clientConfig, SchemaV2.getOldClientConfigPath());
        writeSchemaFile &= copyOldConfigObject(combatConfig, SchemaV5.getCombatConfigPath());
        writeSchemaFile &= copyOldConfigObject(heatingConfig, SchemaV3.getHeatingConfigPath());
        writeSchemaFile &= copyOldConfigObject(weatherConfig, SchemaV6.getOldWeatherPath());
        writeSchemaFile &= copyOldConfigObject(thirstConfig, SchemaV5.getThirstConfigPath());
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