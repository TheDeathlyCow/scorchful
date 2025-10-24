package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.FloatField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class ItemConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("item.json5");

    public static final ConfigClassHandler<ItemConfig> HANDLER = ConfigClassHandler.createBuilder(ItemConfig.class)
            .id(Scorchful.id("common/item"))
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
    @SerialEntry(comment = "Toggle the water breathing effect from Turtle Armor")
    @TickBox
    boolean enableTurtleArmorEffects = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Turtle Armor Lung Capacity Multiplier")
    @SerialEntry(comment = "Multiplies the Lung Capacity attribute value of Turtle Armor.")
    @FloatField(min = 0f)
    float turtleArmorLungCapacityMultiplier = 1.0f;

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }
}