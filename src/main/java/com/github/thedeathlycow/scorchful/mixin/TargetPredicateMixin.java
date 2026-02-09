package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.effect.FearStatusEffect;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TargetingConditions.class)
public class TargetPredicateMixin {
    @WrapOperation(
            method = "test",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/sensing/Sensing;hasLineOfSight(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private boolean canAlwaysSeeFearedTargets(Sensing instance, Entity targetEntity, Operation<Boolean> original) {
        if (FearStatusEffect.isFeared(targetEntity)) {
            return true;
        }
        return original.call(instance, targetEntity);
    }
}
