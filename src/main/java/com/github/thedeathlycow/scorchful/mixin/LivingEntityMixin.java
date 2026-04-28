package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.effect.FearStatusEffect;
import com.github.thedeathlycow.scorchful.event.ScorchfulLivingEntityEvents;
import com.github.thedeathlycow.scorchful.server.SandstormSlowing;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
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
            method = "hurt",
            at = @At("TAIL")
    )
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            ScorchfulLivingEntityEvents.ON_DAMAGED.invoker().onDamaged((LivingEntity) (Object) this, source, amount);
        }
    }

    @Inject(
            method = "aiStep",
            at = @At("TAIL")
    )
    private void afterTickMovement(CallbackInfo ci) {
        ProfilerFiller profiler = this.level().getProfiler();
        profiler.push("scorchful_sandstorm_slow");
        scorchful_wasInSandstorm = SandstormSlowing.tickSandstormSlow(
                (LivingEntity) (Object) this,
                scorchful_wasInSandstorm
        );
        profiler.pop();
    }

    @ModifyReturnValue(
            method = "getVisibilityPercent",
            at = @At("RETURN")
    )
    private double extendMobDetectionWhenFeared(double original) {
        return FearStatusEffect.modifyDetectionDistance((LivingEntity) (Object) this, original);
    }

    @Inject(
            method = "canBeAffected",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockFear(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (!FearStatusEffect.canHaveFear((LivingEntity) (Object) this, effect)) {
            cir.setReturnValue(false);
        }
    }

}
