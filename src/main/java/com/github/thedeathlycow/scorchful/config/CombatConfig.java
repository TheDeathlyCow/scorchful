package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Mth;

@Config(name = Scorchful.MODID + ".combat_config")
public class CombatConfig implements ConfigData {

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    boolean enableDesertVisions = true;

    double fireProtectionHeatResistancePerLevel = 0.125;

    double fearDetectionRangeMultiplier = 2.0;

    @ConfigEntry.Gui.RequiresRestart
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

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        this.fearDetectionRangeMultiplier = Mth.clamp(fearDetectionRangeMultiplier, 0, 128);
        this.impalingDamagePerLevel = Math.max(0f, impalingDamagePerLevel);
    }
}
