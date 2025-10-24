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

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fire Protection heat resistance per level")
    @SerialEntry(comment = "How much Heat Resistance the Fire Protection enchantment should give, per level of Fire Protection.")
    @DoubleField
    double fireProtectionHeatResistancePerLevel = 0.125;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Impaling damage per level")
    @SerialEntry(comment = "How much damage the Impaling enchantment should do to wet entities, per level of Impaling")
    @FloatField(min = 0f)
    float impalingDamagePerLevel = 2.5f;

    @AutoGen(category = CATEGORY)
    @Translate.Name("Fireball throwing type")
    @SerialEntry(comment = "Controls what type of fireball is creating when throwing a fire charge. Small is just like firing from a Dispener and creates a fire on impact. Large is more like a Ghast and creates an explosion, and disabled disables this feature.")
    @EnumCycler
    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }

    public double getFireProtectionHeatResistancePerLevel() {
        return fireProtectionHeatResistancePerLevel;
    }

    public float getImpalingDamagePerLevel() {
        return impalingDamagePerLevel;
    }
}