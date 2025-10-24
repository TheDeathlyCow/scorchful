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
    public static final Path PATH = Scorchful.getConfigDir().resolve("heating.json5");

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

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable Turtle Armor status effects")
    @SerialEntry(comment = "Enable/disable water breathing from Turtle Armor")
    @TickBox
    boolean enableTurtleArmorEffects = true;


    @AutoGen(category = CATEGORY)
    @Translate.Name("Turtle Armor Lung Capacity Multiplier")
    @SerialEntry(comment = "Multiply the Lung Capacity attribute of Turtle Armor.")
    @FloatField(min = 0f)
    float turtleArmorLungCapacityMultiplier = 1.0f;

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }
}
