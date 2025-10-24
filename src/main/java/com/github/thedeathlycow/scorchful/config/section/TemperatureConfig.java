package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class TemperatureConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("common").resolve("temperature.json5");

    public static final ConfigClassHandler<HeatingConfig> HANDLER = ConfigClassHandler.createBuilder(HeatingConfig.class)
            .id(Scorchful.id("common/temperature"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    public static final String GENERAL_CATEGORY_NAME = "general";
    public static final String ENVIRONMENT_CATEGORY_NAME = "environment";
    public static final String TEMPERATURE_SOURCES_CATEGORY_NAME = "temperature_sources";

    @AutoGen(category = GENERAL_CATEGORY_NAME)
    @Translate.Name("Heating multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of all non-environment heating sources like fire and lava.")
    @FloatField(min = 0.0f)
    float heatingMultiplier;

    @AutoGen(category = GENERAL_CATEGORY_NAME)
    @Translate.Name("Cooling multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of all non-environment cooling sources like sweating and powder snow.")
    @FloatField(min = 0.0f)
    float coolingMultiplier;

    @AutoGen(category = GENERAL_CATEGORY_NAME)
    @Translate.Name("Environment temperature multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of an environment temperature change.")
    @FloatField(min = 0.0f)
    float environmentTemperatureMultiplier;

    public float getHeatingMultiplier() {
        return heatingMultiplier;
    }

    public float getCoolingMultiplier() {
        return coolingMultiplier;
    }

    public float getEnvironmentTemperatureMultiplier() {
        return environmentTemperatureMultiplier;
    }

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Enable environment heating")
    @SerialEntry(comment = "Global toggle on all heating from the environment.")
    @TickBox
    boolean enablePassiveHeating = true;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Environment heating tick interval")
    @SerialEntry(comment = "How many ticks should occur between applying heat to a player from the environment. Setting this to large values can be used to slow heat down. This may not be set to a value less than 1.")
    @IntField(min = 1)
    int environmentHeatingTickInterval = 1;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Max environment heating scale")
    @SerialEntry(comment = "The maximum temperature scale that the environment can heat heat players to. Given as a percentage from 0 to 1 (0 = 0%, 1 = 100%)")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float maxEnvironmentHeatingScale = 1.0f;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Min temperature for heat (in °C)")
    @SerialEntry(comment = "Cutoff temperature for overheating in Celsius. Biomes at or above this temperature will apply environment heating to players. May not be less than 25°C.")
    @DoubleField(min = 25.0)
    double minTemperatureForHeatC = 30.0;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Degrees per temperature level increase (in °C/°K)")
    @SerialEntry(comment = "Specifies the number of Celsius/Kelvin degrees the temperature must be above the minimum heat threshold for each one-point increase in player temperature per tick. Must be positive and non-zero.")
    @DoubleField(min = 0.01)
    double degreesCPerTemperatureIncrease = 10.0;

    public boolean isEnablePassiveHeating() {
        return enablePassiveHeating && environmentTemperatureMultiplier > 0f;
    }

    public int getEnvironmentHeatingTickInterval() {
        return environmentHeatingTickInterval;
    }

    public float getMaxEnvironmentHeatingScale() {
        return maxEnvironmentHeatingScale;
    }

    public double getMinTemperatureForHeatC() {
        return minTemperatureForHeatC;
    }

    public double getDegreesCPerTemperatureIncrease() {
        return degreesCPerTemperatureIncrease;
    }
}