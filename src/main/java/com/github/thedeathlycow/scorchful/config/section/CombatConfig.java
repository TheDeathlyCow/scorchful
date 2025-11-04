package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class CombatConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("combat.json5");

    public static final ConfigClassHandler<CombatConfig> HANDLER = ConfigClassHandler.createBuilder(CombatConfig.class)
            .id(Scorchful.id("combat"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable desert visions")
    @SerialEntry(comment = "Toggles hallucinations when overheating in the desert like boats and flowers.")
    @TickBox
    boolean enableDesertVisions = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fear detection range multiplier")
    @SerialEntry(comment = "How much to multiply an entity's (including players) hostile mob detection range by.")
    @DoubleField(min = 0, max = 128)
    double fearDetectionRangeMultiplier = 2.0;

    public boolean enableDesertVisions() {
        return enableDesertVisions;
    }

    public double getFearDetectionRangeMultiplier() {
        return fearDetectionRangeMultiplier;
    }
}
