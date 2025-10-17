package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class Updater {
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

        copyOldConfigObject(clientConfig, ClientConfig.PATH);
        copyOldConfigObject(combatConfig, CombatConfig.PATH);
        copyOldConfigObject(heatingConfig, HeatingConfig.PATH);
        copyOldConfigObject(weatherConfig, WeatherConfig.PATH);
        copyOldConfigObject(thirstConfig, ThirstConfig.PATH);
        copyOldConfigObject(dehydrationConfig, DehydrationConfig.PATH);

        Files.delete(oldConfigPath);
    }

    private static void copyOldConfigObject(JsonObject json, Path dest) throws IOException {
        if (!Files.exists(dest)) {
            json.addProperty("version", 1);
            Files.createDirectories(dest.getParent());
            Files.writeString(dest, json.toString(), StandardOpenOption.CREATE);
        } else {
            Scorchful.LOGGER.warn("Config file {} already exists, skipping upgrade", dest);
        }
    }

    private Updater() {

    }
}