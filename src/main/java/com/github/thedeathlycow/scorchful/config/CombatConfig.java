package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.EnumCycler;
import dev.isxander.yacl3.config.v2.api.autogen.FloatField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

import java.nio.file.Path;

public class CombatConfig {
    static final Path PATH = Scorchful.getConfigDir().resolve("combat.json5");

    public static final ConfigClassHandler<CombatConfig> HANDLER = ConfigClassHandler.createBuilder(CombatConfig.class)
            .id(Scorchful.id("combat"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    private static final String CATEGORY = "combat";

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fireball throwing type")
    @SerialEntry(comment = "Controls what type of fireball is creating when throwing a fire charge. Small is just like firing from a Dispener and creates a fire on impact. Large is more like a Ghast and creates an explosion, and disabled disables this feature.")
    @EnumCycler
    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Enable desert visions")
    @SerialEntry(comment = "Toggles hallucinations when overheating in the desert like boats and flowers.")
    @TickBox
    boolean enableDesertVisions = true;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fire Protection heat resistance per level")
    @SerialEntry(comment = "How much Heat Resistance the Fire Protection enchantment should give, per level of Fire Protection.")
    @FloatField
    double fireProtectionHeatResistancePerLevel = 0.125;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fear detection range multiplier")
    @SerialEntry(comment = "How much to multiply an entity's (including players) hostile mob detection range by.")
    @FloatField(min = 0, max = 128)
    double fearDetectionRangeMultiplier = 2.0;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Impaling damage per level")
    @SerialEntry(comment = "How much damage the Impaling enchantment should do to wet entities, per level of Impaling")
    @FloatField(min = 0)
    float impalingDamagePerLevel = 2.5f;

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }

    public boolean enableDesertVisions() {
        return enableDesertVisions;
    }

    public double getFireProtectionHeatResistancePerLevel() {
        return fireProtectionHeatResistancePerLevel;
    }

    public double getFearDetectionRangeMultiplier() {
        return fearDetectionRangeMultiplier;
    }

    public float getImpalingDamagePerLevel() {
        return impalingDamagePerLevel;
    }
}
