package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.math.MathHelper;

@Config(name = Scorchful.MODID + ".combat_config")
public class CombatConfig implements ConfigData {

    @ConfigEntry.Gui.EnumHandler
    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    boolean enableDesertVisions = true;

    @ConfigEntry.Gui.RequiresRestart
    double defaultArmorHeatResistance = -0.5;

    @ConfigEntry.Gui.RequiresRestart
    double veryHarmfulArmorHeatResistance = -1.0;

    @ConfigEntry.Gui.RequiresRestart
    double protectiveArmorHeatResistance = 0.5;

    @ConfigEntry.Gui.RequiresRestart
    double veryProtectiveArmorHeatResistance = 1.0;

    double fearDetectionRangeMultiplier = 2.0;

    double mesmerizedActivationRadius = 4.0;

    float mesmerizedActivationBaseDamage = 4f;

    float mesmerizedActivationDamagePerLevel = 4f;

    int mesmerizedActionFearLengthPerLevel = 5;

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }

    public boolean enableDesertVisions() {
        return enableDesertVisions;
    }

    public double getDefaultArmorHeatResistance() {
        return defaultArmorHeatResistance;
    }

    public double getVeryHarmfulArmorHeatResistance() {
        return veryHarmfulArmorHeatResistance;
    }

    public double getProtectiveArmorHeatResistance() {
        return protectiveArmorHeatResistance;
    }

    public double getVeryProtectiveArmorHeatResistance() {
        return veryProtectiveArmorHeatResistance;
    }

    public double getFearDetectionRangeMultiplier() {
        return fearDetectionRangeMultiplier;
    }

    public double getMesmerizedActivationRadius() {
        return mesmerizedActivationRadius;
    }

    public float getMesmerizedActivationBaseDamage() {
        return mesmerizedActivationBaseDamage;
    }

    public float getMesmerizedActivationDamagePerLevel() {
        return mesmerizedActivationDamagePerLevel;
    }

    public int getMesmerizedActionFearLengthPerLevel() {
        return 20 * mesmerizedActionFearLengthPerLevel;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        this.fearDetectionRangeMultiplier = MathHelper.clamp(fearDetectionRangeMultiplier, 0, 128);
        this.mesmerizedActivationRadius = Math.max(0f, mesmerizedActivationRadius);
        this.mesmerizedActivationBaseDamage = Math.max(0f, mesmerizedActivationBaseDamage);
        this.mesmerizedActivationDamagePerLevel = Math.max(0f, mesmerizedActivationDamagePerLevel);
        this.mesmerizedActionFearLengthPerLevel = Math.max(0, mesmerizedActionFearLengthPerLevel);
    }
}
