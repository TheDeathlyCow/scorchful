package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.effect.FearEffect;
import com.github.thedeathlycow.scorchful.world.SandstormEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private boolean scorchful_wasInSandstorm = false;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
            method = "aiStep",
            at = @At("TAIL")
    )
    private void afterTickMovement(CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("scorchful_sandstorm_slow");
        scorchful_wasInSandstorm = SandstormEffects.tickSandstormEffects(
                (LivingEntity) (Object) this,
                scorchful_wasInSandstorm
        );
        profiler.pop();
    }
    
    @WrapOperation(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;increaseAirSupply(I)I"
            )
    )
    private int preventAirRefillWhileSuffocating(LivingEntity instance, int currentSupply, Operation<Integer> original) {
        if (scorchful_wasInSandstorm && SandstormEffects.canSuffocate(instance)) {
            return currentSupply;
        } else {
            return original.call(instance, currentSupply);
        }
    }

    @ModifyReturnValue(
            method = "getVisibilityPercent",
            at = @At("RETURN")
    )
    private double extendMobDetectionWhenFeared(double original) {
        return FearEffect.modifyDetectionDistance((LivingEntity) (Object) this, original);
    }

    @Inject(
            method = "canBeAffected",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockFear(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (!FearEffect.canHaveFear((LivingEntity) (Object) this, effect)) {
            cir.setReturnValue(false);
        }
    }
}
