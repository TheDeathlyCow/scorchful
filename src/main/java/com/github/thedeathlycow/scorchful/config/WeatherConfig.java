package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.nio.file.Path;

@Config(name = Scorchful.MODID + ".weather_config")
public class WeatherConfig implements ConfigData {
    static final Path PATH = Scorchful.getConfigDir().resolve("weather.json5");

    public static final ConfigClassHandler<WeatherConfig> HANDLER = ConfigClassHandler.createBuilder(WeatherConfig.class)
            .id(Scorchful.id("thirst"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = "weather";

    private static final int VERSION = 1;

    @Translate.Name("Schema version")
    @SerialEntry(comment = "Config version, do not touch! Changing this value may result in unexpected behaviour.")
    int version = VERSION;

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
}
