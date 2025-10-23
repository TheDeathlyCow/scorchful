package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SchemaV2 {
    public static Path getOldClientConfigPath() {
        return Scorchful.getConfigDir().resolve("client.json5");
    }

    public static void run() throws IOException {
        Path oldPath = getOldClientConfigPath();
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
        JsonCopyHelper.copyBooleanProperty(oldClientConfig, accessibility, "sunHatShadeOpacity");

        // save new files and remove old files
        Path path = Scorchful.getConfigDir().resolve("client/accessibility.json5");
        Files.writeString(path, accessibility.toString(), StandardOpenOption.CREATE);

        path = Scorchful.getConfigDir().resolve("client/display.json5");
        Files.writeString(path, display.toString(), StandardOpenOption.CREATE);

        Files.delete(oldPath);
    }
}