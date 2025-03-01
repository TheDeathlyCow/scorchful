package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.TickContext;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public final class PassiveTemperatureEffects {
    public static void initialize() {
        LivingEntityTemperatureTickEvents.GET_PASSIVE_TEMPERATURE_CHANGE.register(PassiveTemperatureEffects::getPassiveChange);
    }

    private static int getPassiveChange(TickContext<LivingEntity> context) {
        LivingEntity entity = context.affected();

        // don't touch frostiful's effects
        if (entity.isSpectator() || entity.thermoo$getTemperature() < 0) {
            return 0;
        }

        int total = 0;

        ScorchfulConfig config = Scorchful.getConfig();
        total += getIcyFloorTemperatureChange(context, config);

        return total;
    }

    private static int getIcyFloorTemperatureChange(TickContext<LivingEntity> context, ScorchfulConfig config) {
        LivingEntity entity = context.affected();
        BlockState steppingState = entity.getSteppingBlockState();

        if (steppingState.isIn(SBlockTags.HEAVY_ICE) && entity.thermoo$isWarm()) {
            return -config.heatingConfig.getCoolingFromIce();
        }

        return 0;
    }

    private PassiveTemperatureEffects() {

    }
}