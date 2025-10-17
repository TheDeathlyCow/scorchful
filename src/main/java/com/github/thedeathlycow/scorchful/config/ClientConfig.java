package com.github.thedeathlycow.scorchful.config;


import com.github.thedeathlycow.scorchful.Scorchful;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.FloatSlider;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class ClientConfig {
    static final Path PATH = Scorchful.getConfigDir().resolve("client.json5");

    public static final ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
            .id(Scorchful.id("client"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = "client";

    private static final int VERSION = 1;

    @Translate.Name("Schema version")
    @SerialEntry(comment = "Config version, do not touch! Changing this value may result in unexpected behaviour.")
    int version = VERSION;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Do burning heart overlay")
    @SerialEntry(comment = "Toggle the burning heart temperature display on the health bar")
    @TickBox
    boolean doBurningHeartOverlay = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Do soaking overlay")
    @TickBox
    @SerialEntry(comment = "Toggle the soaking display on the health bar")
    boolean doSoakingOverlay = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Do Sun Hat shading")
    @TickBox
    @SerialEntry(comment = "Toggle the darkening effect of the Sun Hat")
    boolean doSunHatShading = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable sound temperature effects")
    @TickBox
    @SerialEntry(comment = "Toggle the sound effects of temperature, particularly the heart beat")
    boolean enableSoundTemperatureEffects = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable wet drip particles")
    @TickBox
    @SerialEntry(comment = "Toggle the dripping particles when wet. This setting overrides Frostiful if installed.")
    boolean enableWetDripParticles = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable Heat Stroke post processing")
    @TickBox
    @SerialEntry(comment = "Toggle the blur and wavey-ness screen effects from the Heat Stroke status effect.")
    boolean enableHeatStrokePostProcessing = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable Fear post processing")
    @TickBox
    @SerialEntry(comment = "Toggle the darkening and desaturation screen effects from the Heat Stroke status effect.")
    boolean enableFearPostProcessing = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Sun Hat shade opacity")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    @SerialEntry(comment = "Controls how dark the sun hat shading is.")
    float sunHatShadeOpacity = 0.2f;

    public float getSunHatShadeOpacity() {
        return doSunHatShading ? sunHatShadeOpacity : 0f;
    }

    public boolean enableSoundTemperatureEffects() {
        return enableSoundTemperatureEffects;
    }

    public boolean enableWetDripParticles() {
        return enableWetDripParticles;
    }

    public boolean enableHeatStrokePostProcessing() {
        return enableHeatStrokePostProcessing;
    }

    public boolean enableFearPostProcessing() {
        return enableFearPostProcessing;
    }

    public boolean doBurningHeartOverlay() {
        return doBurningHeartOverlay;
    }

    public boolean doSoakingOverlay() {
        return doSoakingOverlay;
    }
}
