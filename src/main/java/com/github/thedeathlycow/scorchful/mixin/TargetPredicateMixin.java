package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.effect.FearStatusEffect;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobVisibilityCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin {

    @WrapOperation(
            method = "test",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobVisibilityCache;canSee(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean canAlwaysSeeFearedTargets(MobVisibilityCache instance, Entity targetEntity, Operation<Boolean> original) {
        if (FearStatusEffect.isFeared(targetEntity)) {
            return true;
        }
        return original.call(instance, targetEntity);
    }

}
