package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class WeatherConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("common").resolve("weather.json5");

    public static final ConfigClassHandler<WeatherConfig> HANDLER = ConfigClassHandler.createBuilder(WeatherConfig.class)
            .id(Scorchful.id("common/weather"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;
    public static final String SUFFOCATING_GROUP = "suffocating";

    @AutoGen(category = CATEGORY)
    @Translate.Name("Do Sand Pile accumulation")
    @SerialEntry(comment = "Enable/disable sand piles accumulating on the ground during sandstorms.")
    @TickBox
    boolean doSandPileAccumulation = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Sand Pile accumulation height")
    @SerialEntry(comment = "Sets the maximum height of sand piles that can accumulate during sand storms. Maximum value is 8.")
    @IntSlider(min = 0, max = 8, step = 1)
    int sandPileAccumulationHeight = 1;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Sandstorm Slowness amount (percent from -1 to +1)")
    @SerialEntry(comment = "How much to multiply the total speed of entities on the surface by during Sandstorms. Negative values provide slowness, positive values provide speed.")
    @DoubleSlider(min = -1.0, max = 1.0, step = 0.1)
    double sandstormSlownessAmountPercent = -0.3;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Sandstorm Follow Range reduction amount (percent from -1 to +1)")
    @SerialEntry(comment = "How much to multiply the total follow range of entities on the surface by during Sandstorms. Negative values decrease follow range, positive values increase it.")
    @DoubleSlider(min = -1.0, max = 1.0, step = 0.1)
    double sandstormFollowRangeReductionPercent = -0.5;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable suffocating sandstorms")
    @SerialEntry(comment = "When enabled, Sandstorms will choke the player causing them to suffocate")
    @MasterTickBox({"requireThunderStormsForSuffocation", "suffocatingDamageMultiplier"})
    boolean enableSuffocatingSandstorms = false;

    @AutoGen(category = CATEGORY, group = SUFFOCATING_GROUP)
    @Translate.Name("Require thunder storms for suffocation")
    @SerialEntry(comment = "When enabled, sandstorm suffocation will only apply during Thunderstorms")
    @TickBox
    boolean requireThunderStormsForSuffocation = true;

    @AutoGen(category = CATEGORY, group = SUFFOCATING_GROUP)
    @Translate.Name("Suffocating damage multiplier")
    @SerialEntry(comment = "Multiplies the damage caused by suffocating in a sandstorm")
    @FloatField(min = 0f, format = "%.2f")
    float suffocatingDamageMultiplier = 1.0f;

    public boolean isSandPileAccumulationEnabled() {
        return doSandPileAccumulation;
    }

    public int getSandPileAccumulationHeight() {
        return sandPileAccumulationHeight;
    }

    public double getSandstormSlownessAmountPercent() {
        return sandstormSlownessAmountPercent;
    }

    public double getSandstormFollowRangeReductionPercent() {
        return sandstormFollowRangeReductionPercent;
    }

    public boolean enableSuffocatingSandstorms() {
        return enableSuffocatingSandstorms;
    }

    public boolean requireThunderStormsForSuffocation() {
        return requireThunderStormsForSuffocation;
    }

    public float suffocatingDamageMultiplier() {
        return suffocatingDamageMultiplier;
    }
}
