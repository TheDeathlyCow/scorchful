package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.DoubleField;
import dev.isxander.yacl3.config.v2.api.autogen.IntField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class EntityConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("common").resolve("entity.json5");

    public static final ConfigClassHandler<EntityConfig> HANDLER = ConfigClassHandler.createBuilder(EntityConfig.class)
            .id(Scorchful.id("common/entity"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    public static final String EFFECTS_CATEGORY = "effects";
    public static final String SOAKING_CATEGORY = "soaking";

    @AutoGen(category = EFFECTS_CATEGORY)
    @Translate.Name("Enable desert visions")
    @SerialEntry(comment = "Toggles hallucinations when overheating in the desert like boats and flowers.")
    @TickBox
    boolean enableDesertVisions = true;

    @AutoGen(category = EFFECTS_CATEGORY)
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

    @AutoGen(category = SOAKING_CATEGORY)
    @Translate.Name("Soaking from Splash Potions")
    @SerialEntry(comment = "How much soaking is applied when an entity is hit with any Splash Potion.")
    @IntField
    int soakingFromSplashPotions = 300;

    @AutoGen(category = SOAKING_CATEGORY)
    @Translate.Name("Touching water or rain wetness increase per tick")
    @SerialEntry(comment = "How much to increase wetness by when touching water or rain each tick. Note that submerging yourself in water will fully soak you, regardless of what this is set to.")
    @IntField
    int touchingWaterWetnessIncrease = 1;

    @AutoGen(category = SOAKING_CATEGORY)
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