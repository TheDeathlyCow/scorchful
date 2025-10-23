package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.FloatSlider;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class AccessibilitySettings {
    public static final Path PATH = Scorchful.getConfigDir().resolve("client/accessibility.json5");

    public static final ConfigClassHandler<AccessibilitySettings> HANDLER = ConfigClassHandler.createBuilder(AccessibilitySettings.class)
            .id(Scorchful.id("client/accessibility"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable sound temperature effects")
    @TickBox
    @SerialEntry(comment = "Toggle the sound effects of temperature, particularly the heart beat")
    boolean enableSoundTemperatureEffects = true;

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
    @Translate.Name("Do Sun Hat shading")
    @TickBox
    @SerialEntry(comment = "Toggle the darkening effect of the Sun Hat")
    boolean doSunHatShading = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Sun Hat shade darkness")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    @SerialEntry(comment = "Controls the strength of how dark the sun hat shading is.")
    float sunHatShadeOpacity = 0.2f;

    public boolean enableSoundTemperatureEffects() {
        return enableSoundTemperatureEffects;
    }

    public boolean enableHeatStrokePostProcessing() {
        return enableHeatStrokePostProcessing;
    }

    public boolean enableFearPostProcessing() {
        return enableFearPostProcessing;
    }

    public float getSunHatShadeOpacity() {
        return doSunHatShading ? sunHatShadeOpacity : 0f;
    }
}