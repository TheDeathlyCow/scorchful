package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.FloatSlider;
import dev.isxander.yacl3.config.v2.api.autogen.IntField;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class ThirstConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("thirst.json5");

    public static final ConfigClassHandler<ThirstConfig> HANDLER = ConfigClassHandler.createBuilder(ThirstConfig.class)
            .id(Scorchful.id("thirst"))
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
    @Translate.Name("Temperature from Wetness")
    @SerialEntry(comment = "How much temperature to remove from wet entities, each tick.")
    @IntField
    int temperatureFromWetness = -6;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Water from Refreshing food")
    @SerialEntry(comment = "The amount of body water provided by consuming Refreshing food and drink.")
    @IntField
    int waterFromRefreshingFood = 60;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Water from Sustaining food")
    @SerialEntry(comment = "The amount of body water provided by consuming Sustaining food and drink.")
    @IntField
    int waterFromSustainingFood = 120;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Water from Hydrating food")
    @SerialEntry(comment = "The amount of body water provided by consuming Hydrating food and drink.")
    @IntField
    int waterFromHydratingFood = 300;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Water from Parching food")
    @SerialEntry(comment = "The amount of body water provided by consuming Parching food and drink.")
    @IntField
    int waterFromParchingFood = -120;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Rehydration drink size")
    @SerialEntry(comment = "The threshold for how much body water needs to be collected before Rehydration will automatically rehydrate the player.")
    @IntField
    int rehydrationDrinkSize = 120;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Soaking from Splash Potions")
    @SerialEntry(comment = "How much soaking is applied when an entity is hit with any Splash Potion.")
    @IntField
    int soakingFromSplashPotions = 300;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Touching water or rain wetness increase per tick")
    @SerialEntry(comment = "How much to increase wetness by when touching water or rain each tick. Note that submerging yourself in water will fully soak you, regardless of what this is set to.")
    @IntField
    int touchingWaterWetnessIncrease = 1;

    @AutoGen(category = CATEGORY)
    @Translate.Name("On fire dry rate")
    @SerialEntry(comment = "How many wetness points to remove each tick when on fire.")
    @IntField
    int onFireDryDate = 3;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Humid biome sweating efficiency")
    @SerialEntry(comment = "A multiplier for temperatureFromWetness when the humidity is in the range [65%, 80%). This applies to rainy climates like Jungles, Swamps, etc.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float humidBiomeSweatEfficiency = 1f / 3f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Extra Humid biome sweating efficiency")
    @SerialEntry(comment = "A multiplier for temperatureFromWetness when the humidity is at or above 80%. This applies to rainy climates like Jungles, Swamps during the Wet Season; to cave biomes; and to all non-arid biomes when it rains.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float extraHumidBiomeSweatEfficiency = 1f / 6f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Arid biome sweating efficiency")
    @SerialEntry(comment = "A multiplier for temperatureFromWetness when the humidity is at or below 20%. This applies to arid climates like Deserts, Badlands, and Savannas, as well as The Nether.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float aridBiomeSweatEfficiency = 1.5f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Maximum Rehydration Enchantment efficiency")
    @SerialEntry(comment = "Multiplier for rehydrationDrinkSize that controls the maximum amount of body water provided by a full suit of Rehydration armor.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float maxRehydrationEfficiency = 0.75f;

    public int getTemperatureFromWetness() {
        return temperatureFromWetness;
    }

    public int getWaterFromRefreshingFood() {
        return waterFromRefreshingFood;
    }

    public int getWaterFromSustainingFood() {
        return waterFromSustainingFood;
    }

    public int getWaterFromHydratingFood() {
        return waterFromHydratingFood;
    }

    public int getWaterFromParchingFood() {
        return waterFromParchingFood;
    }

    public int getRehydrationDrinkSize() {
        return rehydrationDrinkSize;
    }

    public int getSoakingFromSplashPotions() {
        return soakingFromSplashPotions;
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getOnFireDryDate() {
        return onFireDryDate;
    }

    public float getHumidBiomeSweatEfficiency() {
        return humidBiomeSweatEfficiency;
    }

    public float getExtraHumidBiomeSweatEfficiency() {
        return extraHumidBiomeSweatEfficiency;
    }

    public float getAridBiomeSweatEfficiency() {
        return aridBiomeSweatEfficiency;
    }

    public float getMaxRehydrationEfficiency() {
        return maxRehydrationEfficiency;
    }
}
