package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.util.Mth;

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

    private static final String CATEGORY = ScorchfulConfig.MAIN_CATEGORY_NAME;
    public static final String SOAKING_CATEGORY = "soaking";

    public static final String EFFECTS_GROUP = "effects";

    @AutoGen(category = CATEGORY, group = EFFECTS_GROUP)
    @Translate.Name("Enable desert visions")
    @SerialEntry(comment = "Toggles hallucinations when overheating in the desert like boats and flowers.")
    @TickBox
    boolean enableDesertVisions = true;

    @AutoGen(category = CATEGORY, group = EFFECTS_GROUP)
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
    @Translate.Name("Soaking from Splash Potions multiplier")
    @SerialEntry(comment = "Multiplies the soaking points that are applied when an entity is hit with any Splash Potion.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float soakingFromSplashPotionsMultiplier = 1.0f;

    @AutoGen(category = SOAKING_CATEGORY)
    @Translate.Name("Touching water or rain wetness increase per tick")
    @SerialEntry(comment = "How many soaking points to add when touching water or rain each tick. Note that submerging yourself in water will fully soak you, regardless of what this is set to.")
    @IntField
    int touchingWaterWetnessIncrease = 1;

    @AutoGen(category = SOAKING_CATEGORY)
    @Translate.Name("On fire dry rate multiplier")
    @SerialEntry(comment = "Multiplies the soaking points that are removed from an entity that is on fire each tick.")
    @FloatField
    float onFireDryRateMultiplier = 1.0f;

    public int getSoakingFromSplashPotions() {
        return Mth.floor(300 * soakingFromSplashPotionsMultiplier);
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getOnFireDryDate() {
        return Mth.floor(3 * onFireDryRateMultiplier);
    }
}