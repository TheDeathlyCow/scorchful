package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchemaV2 {
    public static Path getOldClientConfigPath() {
        return Scorchful.getConfigDir().resolve("client.json5");
    }

    public static void run() throws IOException {
        Path oldPath = getOldClientConfigPath();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            Scorchful.LOGGER.warn("Removing client config from dedicated server, this did nothing anyway.");
            Files.delete(oldPath);
            return;
        }

        String content = Files.readString(oldPath);

        JsonObject oldClientConfig = JsonParser.parseString(content).getAsJsonObject();

        JsonObject accessibility = new JsonObject();
        JsonObject display = new JsonObject();

        // update display settings schema
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, display, "doBurningHeartOverlay");
        JsonCopyHelper.rename(display, "doBurningHeartOverlay", "enableBurningHeartOverlay");

        JsonCopyHelper.copyBooleanProperty(oldClientConfig, display, "doSoakingOverlay");
        JsonCopyHelper.rename(display, "doSoakingOverlay", "enableSoakingOverlay");

        JsonCopyHelper.copyBooleanProperty(oldClientConfig, display, "enableWetDripParticles");

        // update accessibility settings schema
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, accessibility, "enableSoundTemperatureEffects");
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, accessibility, "enableHeatStrokePostProcessing");
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, accessibility, "enableFearPostProcessing");
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, accessibility, "doSunHatShading");
        JsonCopyHelper.rename(accessibility, "doSunHatShading", "enableSunHatShading");
        JsonCopyHelper.copyFloatProperty(oldClientConfig, accessibility, "sunHatShadeOpacity");

        // save new files and remove old files
        Path basePath = Scorchful.getConfigDir().resolve("client");
        Files.createDirectories(basePath);

        Path path = basePath.resolve("accessibility.json5");
        Files.writeString(path, accessibility.toString());

        path = basePath.resolve("display.json5");
        Files.writeString(path, display.toString());

        Files.delete(oldPath);
    }
}