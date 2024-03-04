package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulLivingEntityEvents;
import com.github.thedeathlycow.scorchful.server.SandstormSlowing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
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

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "damage",
            at = @At("TAIL")
    )
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            ScorchfulLivingEntityEvents.ON_DAMAGED.invoker().onDamaged((LivingEntity) (Object) this, source, amount);
        }
    }

    @Inject(
            method = "tickMovement",
            at = @At("TAIL")
    )
    private void afterTickMovement(CallbackInfo ci) {
        Profiler profiler = this.getWorld().getProfiler();
        profiler.push("scorchful_sandstorm_slow");
        scorchful_wasInSandstorm = SandstormSlowing.tickSandstormSlow(
                (LivingEntity) (Object) this,
                scorchful_wasInSandstorm
        );
        profiler.pop();
    }

}
