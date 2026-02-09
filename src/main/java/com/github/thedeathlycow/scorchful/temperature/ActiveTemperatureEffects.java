package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.TemperatureConfig;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

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
        TemperatureConfig config = ScorchfulConfig.getTemperatureConfig();
        total += getOnFireTemperatureChange(entity, config);
        total += getInLavaTemperatureChange(entity, config);
        total += getPowderSnowTemperatureChange(entity, config);

        return total;
    }

    private static int getOnFireTemperatureChange(LivingEntity entity, TemperatureConfig config) {
        if (entity.thermoo$canOverheat() && entity.isOnFire() && !entity.fireImmune()) {
            return config.getOnFireWarmRate(entity.hasEffect(MobEffects.FIRE_RESISTANCE));
        }
        return 0;
    }

    private static int getInLavaTemperatureChange(LivingEntity entity, TemperatureConfig config) {
        if (entity.isInLava()) {
            return config.getInLavaWarmRate();
        } else if (entity.getType() == EntityType.STRIDER) {
            return config.getStriderCooling();
        } else {
            return 0;
        }
    }

    private static int getPowderSnowTemperatureChange(LivingEntity entity, TemperatureConfig config) {
        if (entity.wasInPowderSnow && entity.thermoo$canFreeze()) {
            return config.getPowderSnowCooling();
        }
        return 0;
    }

    private ActiveTemperatureEffects() {

    }
}