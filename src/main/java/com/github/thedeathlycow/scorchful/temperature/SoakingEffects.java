package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.attachment.RehydrationAttachment;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.mixin.accessor.EntityAccessor;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntitySoakingTickEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public final class SoakingEffects {
    public static final ResourceLocation REHYDRATION_TICK_PHASE = Scorchful.id("rehydration_tick");

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
        if (entity.isEyeInFluid(FluidTags.WATER)) {
            return entity.thermoo$getMaxWetTicks();
        }

        ScorchfulConfig config = Scorchful.getConfig();
        int total = 0;

        total += getTouchingWaterChange(entity, config);
        total -= getOnFireChange(entity, config);

        return total;
    }

    private static int getTouchingWaterChange(LivingEntity entity, ScorchfulConfig config) {
        // add wetness when touching, but not submerged in, water or rain
        if (isTouchingWater(entity) || entity.getInBlockState().is(Blocks.WATER_CAULDRON)) {
            return config.thirstConfig.getTouchingWaterWetnessIncrease();
        }

        return 0;
    }

    private static int getOnFireChange(LivingEntity entity, ScorchfulConfig config) {
        return entity.isOnFire()
                ? config.thirstConfig.getOnFireDryDate()
                : 0;
    }

    private static void tickRehydration(EnvironmentTickContext<? extends LivingEntity> context, int wetChange) {
        if (context.affected().thermoo$isWet() && context.affected() instanceof Player player) {
            double rehydrationEfficiency = player.getAttributeValue(SEntityAttributes.REHYDRATION_EFFICIENCY);
            RehydrationAttachment component = player.getData(ScorchfulEntityAttachments.REHYDRATION);
            component.tickRehydration(rehydrationEfficiency, wetChange);
        }
    }

    private static boolean isTouchingWater(LivingEntity entity) {
        if (entity.isInWater()) {
            return true;
        }

        return ((EntityAccessor) entity).scorchful$invokeIsBeingRainedOn()
                && !entity.isHolding(stack -> stack.is(SItemTags.BLOCKS_RAIN_WHEN_HOLDING));
    }

    private SoakingEffects() {

    }
}