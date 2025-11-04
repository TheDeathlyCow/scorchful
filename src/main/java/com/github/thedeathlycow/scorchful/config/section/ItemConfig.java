package com.github.thedeathlycow.scorchful.config.section;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.Translate;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.util.math.MathHelper;

import java.nio.file.Path;

public class ItemConfig {
    public static final Path PATH = Scorchful.getConfigDir().resolve("common").resolve("item.json5");

    public static final ConfigClassHandler<ItemConfig> HANDLER = ConfigClassHandler.createBuilder(ItemConfig.class)
            .id(Scorchful.id("common/item"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(PATH)
                            .setJson5(true)
                            .build()
            )
            .build();

    public static final String TOOLS_AND_ARMOR_CATEGORY_NAME = "tools_and_armor";
    public static final String CONSUMABLE_CATEGORY_NAME = "consumable";
    public static final String MISC_CATEGORY_NAME = "misc";

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Enable Turtle Armor status effects")
    @SerialEntry(comment = "Toggle the water breathing effect from Turtle Armor")
    @TickBox
    boolean enableTurtleArmorEffects = true;

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Turtle Armor Lung Capacity Multiplier")
    @SerialEntry(comment = "Multiplies the Lung Capacity attribute value of Turtle Armor.")
    @FloatField(min = 0f)
    float turtleArmorLungCapacityMultiplier = 1.0f;

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Fire Protection heat resistance per level")
    @SerialEntry(comment = "How much Heat Resistance the Fire Protection enchantment should give, per level of Fire Protection.")
    @DoubleField
    double fireProtectionHeatResistancePerLevel = 0.125;

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Impaling damage per level")
    @SerialEntry(comment = "How much damage the Impaling enchantment should do to wet entities, per level of Impaling")
    @FloatField(min = 0f)
    float impalingDamagePerLevel = 2.5f;

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Rehydration drink size multiplier")
    @SerialEntry(comment = "The threshold for how much body water needs to be collected before Rehydration will automatically rehydrate the player.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float rehydrationDrinkSizeMultiplier = 1.0f;

    @AutoGen(category = TOOLS_AND_ARMOR_CATEGORY_NAME)
    @Translate.Name("Maximum Rehydration Enchantment efficiency")
    @SerialEntry(comment = "Multiplier for the Rehydration drink size that controls the maximum amount of body water provided by a full suit of Rehydration armor.")
    @FloatSlider(min = 0f, max = 1f, step = 0.05f)
    float maxRehydrationEfficiency = 0.75f;

    public boolean isTurtleArmorEffectsEnabled() {
        return enableTurtleArmorEffects;
    }

    public float getTurtleArmorLungCapacityMultiplier() {
        return turtleArmorLungCapacityMultiplier;
    }

    public double getFireProtectionHeatResistancePerLevel() {
        return fireProtectionHeatResistancePerLevel;
    }

    public float getImpalingDamagePerLevel() {
        return impalingDamagePerLevel;
    }

    public int getRehydrationDrinkSize() {
        return MathHelper.floor(120 * rehydrationDrinkSizeMultiplier);
    }

    public float getMaxRehydrationEfficiency() {
        return maxRehydrationEfficiency;
    }

    @AutoGen(category = CONSUMABLE_CATEGORY_NAME)
    @Translate.Name("Water from Refreshing food")
    @SerialEntry(comment = "Multiplies the amount of body water provided by consuming Refreshing food and drink.")
    @FloatField
    float refreshingWaterMultiplier = 1.0f;

    @AutoGen(category = CONSUMABLE_CATEGORY_NAME)
    @Translate.Name("Water from Sustaining food")
    @SerialEntry(comment = "Multiplies the amount of body water provided by consuming Sustaining food and drink.")
    @FloatField
    float sustainingWaterMultiplier = 1.0f;

    @AutoGen(category = CONSUMABLE_CATEGORY_NAME)
    @Translate.Name("Water from Hydrating food")
    @SerialEntry(comment = "Multiplies the amount of body water provided by consuming Hydrating food and drink.")
    @FloatField
    float hydratingWaterMultiplier = 1.0f;

    @AutoGen(category = CONSUMABLE_CATEGORY_NAME)
    @Translate.Name("Water from Parching food")
    @SerialEntry(comment = "Multiplies the amount of body water lost from consuming Hydrating food and drink.")
    @FloatField
    float parchingWaterMultiplier = 1.0f;

    public int getWaterFromRefreshingFood() {
        return MathHelper.floor(60 * refreshingWaterMultiplier);
    }

    public int getWaterFromSustainingFood() {
        return MathHelper.floor(120 * sustainingWaterMultiplier);
    }

    public int getWaterFromHydratingFood() {
        return MathHelper.floor(300 * hydratingWaterMultiplier);
    }

    public int getWaterFromParchingFood() {
        return MathHelper.floor(-120 * parchingWaterMultiplier);
    }

    @AutoGen(category = MISC_CATEGORY_NAME)
    @Translate.Name("Fireball throwing type")
    @SerialEntry(comment = "Controls what type of fireball is creating when throwing a fire charge. Small is just like firing from a Dispener and creates a fire on impact. Large is more like a Ghast and creates an explosion, and disabled disables this feature.")
    @EnumCycler
    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }
}