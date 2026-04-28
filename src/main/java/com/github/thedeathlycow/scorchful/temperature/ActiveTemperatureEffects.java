package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;

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
        ScorchfulConfig config = Scorchful.getConfig();
        total += getOnFireTemperatureChange(entity, config);
        total += getInLavaTemperatureChange(entity, config);
        total += getPowderSnowTemperatureChange(entity, config);

        return total;
    }

    private static int getOnFireTemperatureChange(LivingEntity entity, ScorchfulConfig config) {
        if (entity.thermoo$canOverheat() && entity.isOnFire() && !entity.fireImmune()) {
            return entity.hasEffect(MobEffects.FIRE_RESISTANCE)
                    ? config.heatingConfig.getOnFireWarmRateWithFireResistance()
                    : config.heatingConfig.getOnFireWarmRate();
        }
        return 0;
    }

    private static int getInLavaTemperatureChange(LivingEntity entity, ScorchfulConfig config) {
        if (entity.isInLava()) {
            return config.heatingConfig.getInLavaWarmRate();
        } else if (entity.getType() == EntityType.STRIDER) {
            return -config.heatingConfig.getStriderOutOfLavaCoolRate();
        } else {
            return 0;
        }
    }

    private static int getPowderSnowTemperatureChange(LivingEntity entity, ScorchfulConfig config) {
        if (entity.wasInPowderSnow && entity.thermoo$canFreeze()) {
            return -config.heatingConfig.getPowderSnowCoolRate();
        }
        return 0;
    }

    private ActiveTemperatureEffects() {

    }
}