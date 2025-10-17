package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.PlayerWaterComponent;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.IntSlider;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

/**
 * Config for changes to thirst system when using Dehydration
 */
public class DehydrationConfig {
    static final Path PATH = Scorchful.getConfigDir().resolve("compat").resolve("dehydration.json5");

    public static final ConfigClassHandler<DehydrationConfig> HANDLER = ConfigClassHandler.createBuilder(DehydrationConfig.class)
            .id(Scorchful.id("compat/dehydration"))
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

    /**
     * Don't lose water to sweat when below this level.
     */
    @AutoGen(category = CATEGORY)
    @Translate.Name("Minimum water level required for sweating")
    @SerialEntry(comment = "The minimum number of water points needed to be able to sweat (similar to how much hunger you need to heal)")
    @IntSlider(min = 0, max = 20, step = 1)
    int minWaterLevelForSweat = 16;

    /**
     * How this value was derived:
     * <p>
     * Scorchful only takes 4 points of thirst. 4 points of dehydration per thirst so 4 * 4 = 16 dehydration points.
     * <p>
     * There are 300 body water points in Scorchful.
     * <p>
     * 16 dehydration points = 300 body water points. So the conversion factor is 16 dp / 300 bw.
     */
    public float getDehydrationConsumedBySweat() {
        // adapt based on size of bar
        float pointRange = (20f - minWaterLevelForSweat) * 4f;
        return pointRange / PlayerWaterComponent.MAX_WATER;
    }

    public int getMinWaterLevelForSweat() {
        return minWaterLevelForSweat;
    }

    public int getMaxWaterLost() {
        return 20 - minWaterLevelForSweat;
    }

    public int getRehydrationDrinkSize() {
        return PlayerWaterComponent.MAX_WATER;
    }
}
