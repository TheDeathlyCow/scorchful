package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class HeatingConfig {
    static final Path PATH = Scorchful.getConfigDir().resolve("heating.json5");

    public static final ConfigClassHandler<HeatingConfig> HANDLER = ConfigClassHandler.createBuilder(HeatingConfig.class)
            .id(Scorchful.id("heating"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;

    private static final int VERSION = 1;

    @Translate.Name("Schema version")
    @SerialEntry(comment = "Config version, do not touch! Changing this value may result in unexpected behaviour.")
    int version = VERSION;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Do passive heating")
    @SerialEntry(comment = "Enable/disable passive environmental heating.")
    @TickBox
    boolean doPassiveHeating = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Passive heating tick interval")
    @SerialEntry(comment = "How many ticks should occur between applying heat to a player from the environment. Setting this to large values can be used to slow heat down.")
    @IntField(min = 1)
    int passiveHeatingTickInterval = 1;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Max passive heating scale")
    @SerialEntry(comment = "The maximum scale that the environment can heat heat players to. Given as a percentage from 0 to 1 (0 = 0%, 1 = 100%)")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float maxPassiveHeatingScale = 1.0f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable Turtle Armor status effects")
    @SerialEntry(comment = "Enable/disable water breathing from Turtle Armor")
    @TickBox
    boolean enableTurtleArmorEffects = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Min temperature for heat (in °C)")
    @SerialEntry(comment = "Cutoff temperature for overheating in Celsius. Biomes at or above this temperature will apply environment heating to players. May not be less than 25°C.")
    @DoubleField(min = 25.0)
    double minTemperatureForHeatC = 30.0;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Degrees per temperature level increase (in °C/°K)")
    @SerialEntry(comment = "Specifies the number of Celsius/Kelvin degrees the temperature must be above the minimum heat threshold for each one-point increase in player temperature per tick. Must be positive and non-zero.")
    @DoubleField(min = 0.01)
    double degreesCPerTemperatureIncrease = 10.0;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Environment temperature multiplier")
    @SerialEntry(comment = "Multiplies the final temperature point change of an environment temperature change.")
    @DoubleField(min = 0.0)
    double environmentTemperatureMultiplier = 1.0;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Cooling from ice")
    @SerialEntry(comment = "How many temperature points to remove each tick from an entity standing on packed or blue ice.")
    @IntField
    int coolingFromIce = 12;

    @AutoGen(category = CATEGORY)
    @Translate.Name("On fire warm rate")
    @SerialEntry(comment = "How much to increase the temperature of entities on fire each tick.")
    @IntField
    int onFireWarmRate = 24;

    @AutoGen(category = CATEGORY)
    @Translate.Name("On fire warm rate with Fire Resistance")
    @SerialEntry(comment = "How much to increase the temperature of entities on fire each tick, when they have Fire Resistance.")
    @IntField
    int onFireWarmRateWithFireResistance = 6;

    @AutoGen(category = CATEGORY)
    @Translate.Name("In lava warm rate")
    @SerialEntry(comment = "The temperature change to apply to entities swimming in Lava each tick.")
    @IntField
    int inLavaWarmRate = 24;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Strider out of lava cool rate")
    @SerialEntry(comment = "The cooling to apply only to Striders each tick they are not in Lava")
    @IntField
    int striderOutOfLavaCoolRate = 24;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Powder Snow cool rate when warm")
    @SerialEntry(comment = "The cooling to apply to entities in Powder Snow each tick (only applies when their temperature is > 0).")
    @IntField
    int powderSnowCoolRate = 24;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fireball strike heat")
    @SerialEntry(comment = "The amount of heat to apply to entities when struck by a fire ball / fire charge.")
    @IntField
    int fireballHeat = 1000;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Turtle Armor Lung Capacity Multiplier")
    @SerialEntry(comment = "Multiply the Lung Capacity attribute of Turtle Armor.")
    @FloatField(min = 0f)
    float turtleArmorLungCapacityMultiplier = 1.0f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Temperature from cooling food")
    @SerialEntry(comment = "Temperature change to apply to players after eating items with the tag #scorchful:is_cooling_food.")
    @IntField
    int temperatureFromCoolingFood = -1260;

    public boolean doPassiveHeating() {
        return doPassiveHeating;
    }

    public int getPassiveHeatingTickInterval() {
        return passiveHeatingTickInterval;
    }

    public float getMaxPassiveHeatingScale() {
        return maxPassiveHeatingScale;
    }

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public double getMinTemperatureForHeatC() {
        return minTemperatureForHeatC;
    }

    public double getDegreesCPerTemperatureIncrease() {
        return degreesCPerTemperatureIncrease;
    }

    public double getEnvironmentTemperatureMultiplier() {
        return environmentTemperatureMultiplier;
    }

    public int getCoolingFromIce() {
        return coolingFromIce;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }

    public int getOnFireWarmRateWithFireResistance() {
        return onFireWarmRateWithFireResistance;
    }

    public int getInLavaWarmRate() {
        return inLavaWarmRate;
    }

    public int getStriderOutOfLavaCoolRate() {
        return striderOutOfLavaCoolRate;
    }

    public int getPowderSnowCoolRate() {
        return powderSnowCoolRate;
    }

    public int getFireballHeat() {
        return fireballHeat;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }

    public int getTemperatureFromCoolingFood() {
        return temperatureFromCoolingFood;
    }
}
