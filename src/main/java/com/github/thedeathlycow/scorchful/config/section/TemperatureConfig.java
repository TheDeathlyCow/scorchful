package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.util.math.MathHelper;

import java.nio.file.Path;

public class TemperatureConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("common").resolve("temperature.json5");

    public static final ConfigClassHandler<TemperatureConfig> HANDLER = ConfigClassHandler.createBuilder(TemperatureConfig.class)
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
    @FloatField(min = 0.0f, format = "%.2f")
    float heatingMultiplier = 1.0f;

    @AutoGen(category = GENERAL_CATEGORY_NAME)
    @Translate.Name("Cooling multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of all non-environment cooling sources like sweating and powder snow.")
    @FloatField(min = 0.0f, format = "%.2f")
    float coolingMultiplier = 1.0f;

    @AutoGen(category = GENERAL_CATEGORY_NAME)
    @Translate.Name("Environment temperature multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of an environment temperature change.")
    @FloatField(min = 0.0f, format = "%.2f")
    float environmentTemperatureMultiplier = 1.0f;

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
    boolean enableEnvironmentHeating = true;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Environment heating tick interval")
    @SerialEntry(comment = "How many ticks should occur between applying heat to a player from the environment. Setting this to large values can be used to slow heat down. This may not be set to a value less than 1.")
    @IntField(min = 1)
    int environmentHeatingTickInterval = 1;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Max environment heating scale")
    @SerialEntry(comment = "The maximum temperature scale that the environment can heat players to. Given as a percentage from 0 to 1 (0 = 0%, 1 = 100%)")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f, format = "%.2f")
    float maxEnvironmentHeatingScale = 1.0f;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Minimum temperature for heat (in °C)")
    @SerialEntry(comment = "Cutoff temperature for overheating in Celsius. Biomes at or above this temperature will apply environment heating to players. May not be less than 25°C.")
    @DoubleField(min = 25.0)
    double minTemperatureForHeatC = 30.0;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Degrees per temperature level increase (in °C/°K)")
    @SerialEntry(comment = "Specifies the number of Celsius/Kelvin degrees the temperature must be above the minimum heat threshold for each one-point increase in player temperature per tick. Must be positive and non-zero.")
    @DoubleField(min = 0.01)
    double degreesCPerTemperatureIncrease = 10.0;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Sweating efficiency")
    @SerialEntry(comment = "A multiplier for overall sweating efficiency. Large values mean you will cool down faster from being wet.")
    @FloatSlider(min = 0f, max = 2f, step = 0.05f, format = "%.2f")
    float sweatEfficiency = 1.0f;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Humid biome sweating efficiency multiplier")
    @SerialEntry(comment = "A multiplier for sweating efficiency when the relative humidity is between 65% and 80^. This applies to rainy climates like Jungles, Swamps, etc.")
    @FloatSlider(min = 0f, max = 2f, step = 0.05f, format = "%.2f")
    float humidBiomeSweatEfficiencyMultiplier = 1.0f;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Extra Humid biome sweating efficiency multiplier")
    @SerialEntry(comment = "A multiplier for sweating efficiency when the relative humidity is at or above 80%. This applies to rainy climates like Jungles, Swamps during the Wet Season (if a seasons mod is installed); to cave biomes; and to all non-arid biomes when it rains.")
    @FloatSlider(min = 0f, max = 2f, step = 0.05f, format = "%.2f")
    float extraHumidBiomeSweatEfficiencyMultiplier = 1.0f;

    @AutoGen(category = ENVIRONMENT_CATEGORY_NAME)
    @Translate.Name("Arid biome sweating efficiency multiplier")
    @SerialEntry(comment = "A multiplier for sweating efficiency when the relative humidity is at or below 20%. This applies to arid climates like Deserts, Badlands, and Savannas, as well as The Nether.")
    @FloatSlider(min = 0f, max = 2f, step = 0.05f, format = "%.2f")
    float aridBiomeSweatEfficiencyMultiplier = 1.0f;

    public boolean isEnableEnvironmentHeating() {
        return enableEnvironmentHeating && environmentTemperatureMultiplier > 0f;
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

    public float getHumidBiomeSweatEfficiency() {
        return (1f / 3f) * humidBiomeSweatEfficiencyMultiplier * sweatEfficiency;
    }

    public float getExtraHumidBiomeSweatEfficiency() {
        return (1f / 6f) * extraHumidBiomeSweatEfficiencyMultiplier * sweatEfficiency;
    }

    public float getAridBiomeSweatEfficiency() {
        return 1.5f * aridBiomeSweatEfficiencyMultiplier * sweatEfficiency;
    }

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Fireball temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied to entities when struck by a Fire Charge.")
    @FloatField(min = 0.0f, format = "%.2f")
    float fireballTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Burning temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied to entities on fire each tick.")
    @FloatField(min = 0.0f, format = "%.2f")
    float burningTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Burning temperature multiplier with Fire Resistance")
    @SerialEntry(comment = "Multiplies the temperature point change applied to entities that are on fire, but also have the Fire Resistance effect, each tick.")
    @FloatField(min = 0.0f, format = "%.2f")
    float burningTemperatureMultiplierWithFireResistance = 0.25f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("In lava warm rate temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied to entities swimming in Lava each tick.")
    @FloatField(min = 0.0f, format = "%.2f")
    float inLavaTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Soaked temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied entities that are wet, each tick.")
    @FloatField(min = 0.0f, format = "%.2f")
    float soakedTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Powder Snow temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied to entities in Powder Snow each tick. This only applies to entities that are currently warm.")
    @FloatField(min = 0.0f, format = "%.2f")
    float powderSnowTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Ice temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied each tick from entities standing on ice.")
    @FloatField(min = 0.0f, format = "%.2f")
    float iceTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Cooling food temperature multiplier")
    @SerialEntry(comment = "Multiplies the temperature point change applied to players after eating items with the tag #scorchful:is_cooling_food.")
    @FloatField(min = 0.0f, format = "%.2f")
    float coolingFoodTemperatureMultiplier = 1.0f;

    @AutoGen(category = TEMPERATURE_SOURCES_CATEGORY_NAME)
    @Translate.Name("Strider cooling temperature multiplier")
    @SerialEntry(comment = "Multiplies the cooling to applied only to Striders each tick they are not in Lava.")
    @FloatField(min = 0.0f, format = "%.2f")
    float striderCoolingTemperatureMultiplier = 1.0f;


    public int getFireballHeat() {
        return MathHelper.floor(1000 * fireballTemperatureMultiplier * getHeatingMultiplier());
    }

    public int getOnFireWarmRate(boolean hasFireResistance) {
        float multiplier = hasFireResistance ? burningTemperatureMultiplierWithFireResistance : burningTemperatureMultiplier;
        return MathHelper.floor(24 * multiplier * getHeatingMultiplier());
    }

    public int getInLavaWarmRate() {
        return MathHelper.floor(24 * inLavaTemperatureMultiplier * getHeatingMultiplier());
    }

    public int getTemperatureFromWetness() {
        return MathHelper.floor(-6 * soakedTemperatureMultiplier * getCoolingMultiplier());
    }

    public int getPowderSnowCooling() {
        return MathHelper.floor(-24 * powderSnowTemperatureMultiplier * getCoolingMultiplier());
    }

    public int getIceCooling() {
        return MathHelper.floor(-12 * iceTemperatureMultiplier * getCoolingMultiplier());
    }

    public int getFoodCooling() {
        return MathHelper.floor(-1260 * coolingFoodTemperatureMultiplier * getCoolingMultiplier());
    }

    public int getStriderCooling() {
        return MathHelper.floor(-24 * striderCoolingTemperatureMultiplier * getCoolingMultiplier());
    }
}