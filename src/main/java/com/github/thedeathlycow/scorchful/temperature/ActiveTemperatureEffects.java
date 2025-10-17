package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.config.section.HeatingConfig;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;

public final class ActiveTemperatureEffects {
    public static void initialize() {
        LivingEntityTemperatureTickEvents.GET_ACTIVE_TEMPERATURE_CHANGE.register(ActiveTemperatureEffects::getActiveChange);
    }

    private static int getActiveChange(EnvironmentTickContext<? extends LivingEntity> context) {
        LivingEntity entity = context.affected();

        // don't touch frostiful's effects
        if (entity.isSpectator() || entity.thermoo$getTemperature() < 0) {
            return 0;
        }

        int total = 0;
        HeatingConfig config = ScorchfulConfig.getHeatingConfig();
        total += getOnFireTemperatureChange(entity, config);
        total += getInLavaTemperatureChange(entity, config);
        total += getPowderSnowTemperatureChange(entity, config);

        return total;
    }

    private static int getOnFireTemperatureChange(LivingEntity entity, HeatingConfig config) {
        if (entity.thermoo$canOverheat() && entity.isOnFire() && !entity.isFireImmune()) {
            return entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)
                    ? config.getOnFireWarmRateWithFireResistance()
                    : config.getOnFireWarmRate();
        }
        return 0;
    }

    private static int getInLavaTemperatureChange(LivingEntity entity, HeatingConfig config) {
        if (entity.isInLava()) {
            return config.getInLavaWarmRate();
        } else if (entity.getType() == EntityType.STRIDER) {
            return -config.getStriderOutOfLavaCoolRate();
        } else {
            return 0;
        }
    }

    private static int getPowderSnowTemperatureChange(LivingEntity entity, HeatingConfig config) {
        if (entity.wasInPowderSnow && entity.thermoo$canFreeze()) {
            return -config.getPowderSnowCoolRate();
        }
        return 0;
    }

    private ActiveTemperatureEffects() {

    }
}