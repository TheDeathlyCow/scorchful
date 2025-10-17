package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.RehydrationComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ThirstConfig;
import com.github.thedeathlycow.scorchful.mixin.accessor.EntityAccessor;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntitySoakingTickEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;

public final class SoakingEffects {
    public static final Identifier REHYDRATION_TICK_PHASE = Scorchful.id("rehydration_tick");

    public static void initialize() {
        LivingEntitySoakingTickEvents.GET_SOAKING_CHANGE.register(SoakingEffects::getSoakingChange);

        // place in an earlier phase in case of cancellation
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE.addPhaseOrdering(REHYDRATION_TICK_PHASE, Event.DEFAULT_PHASE);
        LivingEntitySoakingTickEvents.ALLOW_SOAKING_CHANGE.register(
                REHYDRATION_TICK_PHASE,
                (context, soakingChange) -> {
                    SoakingEffects.tickRehydration(context, soakingChange);
                    return TriState.DEFAULT;
                }
        );
    }

    private static int getSoakingChange(EnvironmentTickContext<? extends LivingEntity> context) {
        if (context.affected().isSpectator()) {
            return 0;
        }

        LivingEntity entity = context.affected();

        // fully soak in water
        if (entity.isSubmergedIn(FluidTags.WATER)) {
            return entity.thermoo$getMaxWetTicks();
        }

        ThirstConfig config = ScorchfulConfig.getThirstConfig();
        int total = 0;

        total += getTouchingWaterChange(entity, config);
        total -= getOnFireChange(entity, config);

        return total;
    }

    private static int getTouchingWaterChange(LivingEntity entity, ThirstConfig config) {
        // add wetness when touching, but not submerged in, water or rain
        if (isTouchingWater(entity) || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            return config.getTouchingWaterWetnessIncrease();
        }

        return 0;
    }

    private static int getOnFireChange(LivingEntity entity, ThirstConfig config) {
        return entity.isOnFire()
                ? config.getOnFireDryDate()
                : 0;
    }

    private static void tickRehydration(EnvironmentTickContext<? extends LivingEntity> context, int wetChange) {
        if (context.affected() instanceof PlayerEntity player) {
            double rehydrationEfficiency = player.getAttributeValue(SEntityAttributes.REHYDRATION_EFFICIENCY);
            RehydrationComponent component = ScorchfulComponents.REHYDRATION.get(player);
            component.tickRehydration(rehydrationEfficiency, wetChange);
        }
    }

    private static boolean isTouchingWater(LivingEntity entity) {
        if (entity.isTouchingWater()) {
            return true;
        }

        return ((EntityAccessor) entity).scorchful$invokeIsBeingRainedOn()
                && !entity.isHolding(stack -> stack.isIn(SItemTags.BLOCKS_RAIN_WHEN_HOLDING));
    }

    private SoakingEffects() {

    }
}