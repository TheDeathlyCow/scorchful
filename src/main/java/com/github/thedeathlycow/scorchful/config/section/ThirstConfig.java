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

    public int getSoakingFromSplashPotions() {
        return soakingFromSplashPotions;
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getOnFireDryDate() {
        return onFireDryDate;
    }
}
