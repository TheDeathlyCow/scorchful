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

    double defaultArmorHeatResistanceMultiplier = -1;

    double veryHarmfulArmorHeatResistanceMultiplier = -2.0;

    double protectiveArmorHeatResistanceMultiplier = 1.0;

    double veryProtectiveArmorHeatResistanceMultiplier = 2.0;

    double fearDetectionRangeMultiplier = 2.0;

    @ConfigEntry.Gui.RequiresRestart
    float impalingDamagePerLevel = 2.5f;

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }

    public boolean enableDesertVisions() {
        return enableDesertVisions;
    }

    public double getDefaultArmorHeatResistanceMultiplier() {
        return defaultArmorHeatResistanceMultiplier;
    }

    public double getVeryHarmfulArmorHeatResistanceMultiplier() {
        return veryHarmfulArmorHeatResistanceMultiplier;
    }

    public double getProtectiveArmorHeatResistanceMultiplier() {
        return protectiveArmorHeatResistanceMultiplier;
    }

    public double getVeryProtectiveArmorHeatResistanceMultiplier() {
        return veryProtectiveArmorHeatResistanceMultiplier;
    }

    public double getFearDetectionRangeMultiplier() {
        return fearDetectionRangeMultiplier;
    }

    public float getImpalingDamagePerLevel() {
        return impalingDamagePerLevel;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        this.fearDetectionRangeMultiplier = MathHelper.clamp(fearDetectionRangeMultiplier, 0, 128);
        this.impalingDamagePerLevel = Math.max(0f, impalingDamagePerLevel);
    }
}
