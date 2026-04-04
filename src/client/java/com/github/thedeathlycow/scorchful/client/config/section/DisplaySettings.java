package com.github.thedeathlycow.scorchful.client.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class DisplaySettings {
    public static final Path PATH = Scorchful.getConfigDir().resolve("client/display.json5");

    public static final ConfigClassHandler<DisplaySettings> HANDLER = ConfigClassHandler.createBuilder(DisplaySettings.class)
            .id(Scorchful.id("client/display"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable burning heart overlay")
    @SerialEntry(comment = "Toggle the burning heart temperature display on the health bar")
    @TickBox
    boolean enableBurningHeartOverlay = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable soaking overlay")
    @TickBox
    @SerialEntry(comment = "Toggle the soaking display on the health bar")
    boolean enableSoakingOverlay = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable wet drip particles")
    @TickBox
    @SerialEntry(comment = "Toggle the dripping particles when wet. This setting overrides Frostiful if installed.")
    boolean enableWetDripParticles = true;

    public boolean enableBurningHeartOverlay() {
        return enableBurningHeartOverlay;
    }

    public boolean enableSoakingOverlay() {
        return enableSoakingOverlay;
    }

    public boolean enableWetDripParticles() {
        return enableWetDripParticles;
    }
}