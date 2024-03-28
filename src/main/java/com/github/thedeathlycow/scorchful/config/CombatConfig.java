package com.github.thedeathlycow.scorchful.config;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Scorchful.MODID + ".combat_config")
public class CombatConfig implements ConfigData {

    FireChargeThrower.FireballFactory fireBallThrownType = FireChargeThrower.FireballFactory.SMALL;

    public FireChargeThrower.FireballFactory getFireBallThrownType() {
        return fireBallThrownType;
    }
}
