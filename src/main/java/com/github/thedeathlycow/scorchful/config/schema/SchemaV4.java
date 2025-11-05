package com.github.thedeathlycow.scorchful.config.schema;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SchemaV4 {
    public static Path getCombatConfigPath() {
        return Scorchful.getConfigDir().resolve("combat.json5");
    }

    public static Path getThirstConfigPath() {
        return Scorchful.getConfigDir().resolve("thirst.json5");
    }

    public static void run(int originalSchemaVersion) throws IOException {
        JsonObject oldHeatingConfig = JsonCopyHelper.read(SchemaV3.getHeatingConfigPath());
        JsonObject oldCombatConfig = JsonCopyHelper.read(getCombatConfigPath());
        JsonObject oldThirstConfig = JsonCopyHelper.read(getThirstConfigPath());

        var itemConfig = new JsonObject();

        // heat config
        JsonCopyHelper.copyBooleanProperty(oldHeatingConfig, itemConfig, "enableTurtleArmorEffects");
        JsonCopyHelper.copyFloatProperty(oldHeatingConfig, itemConfig, "turtleArmorLungCapacityMultiplier");

        // combat config
        JsonCopyHelper.copyStringProperty(oldCombatConfig, itemConfig, "fireBallThrownType");
        JsonCopyHelper.copyDoubleProperty(oldCombatConfig, itemConfig, "fireProtectionHeatResistancePerLevel");
        JsonCopyHelper.copyFloatProperty(oldCombatConfig, itemConfig, "impalingDamagePerLevel");

        // thirst config
        JsonCopyHelper.copyIntProperty(oldThirstConfig, itemConfig, "waterFromRefreshingFood");
        JsonCopyHelper.copyIntProperty(oldThirstConfig, itemConfig, "waterFromSustainingFood");
        JsonCopyHelper.copyIntProperty(oldThirstConfig, itemConfig, "waterFromHydratingFood");
        JsonCopyHelper.copyIntProperty(oldThirstConfig, itemConfig, "waterFromParchingFood");
        JsonCopyHelper.copyIntProperty(oldThirstConfig, itemConfig, "rehydrationDrinkSize");
        JsonCopyHelper.copyFloatProperty(oldThirstConfig, itemConfig, "maxRehydrationEfficiency");

        JsonCopyHelper.rename(itemConfig, "waterFromRefreshingFood", "refreshingWaterMultiplier");
        JsonCopyHelper.rename(itemConfig, "waterFromSustainingFood", "sustainingWaterMultiplier");
        JsonCopyHelper.rename(itemConfig, "waterFromHydratingFood", "hydratingWaterMultiplier");
        JsonCopyHelper.rename(itemConfig, "waterFromParchingFood", "parchingWaterMultiplier");
        JsonCopyHelper.rename(itemConfig, "rehydrationDrinkSize", "rehydrationDrinkSizeMultiplier");

        JsonCopyHelper.convertIntToFloatMultiplier(itemConfig, "refreshingWaterMultiplier", 60);
        JsonCopyHelper.convertIntToFloatMultiplier(itemConfig, "sustainingWaterMultiplier", 120);
        JsonCopyHelper.convertIntToFloatMultiplier(itemConfig, "hydratingWaterMultiplier", 300);
        JsonCopyHelper.convertIntToFloatMultiplier(itemConfig, "parchingWaterMultiplier", 120);
        JsonCopyHelper.convertIntToFloatMultiplier(itemConfig, "rehydrationDrinkSizeMultiplier", 120);

        // clean up files
        Path basePath = Scorchful.getConfigDir().resolve("common");
        Files.createDirectories(basePath);

        Path path = basePath.resolve("item.json5");
        Files.writeString(path, itemConfig.toString());

        Files.writeString(getCombatConfigPath(), oldCombatConfig.toString());

        Files.writeString(getThirstConfigPath(), oldThirstConfig.toString());

        Files.delete(SchemaV3.getHeatingConfigPath());
    }

    private SchemaV4() {

    }
}