package com.github.thedeathlycow.scorchful.mixin.compat.frostiful.absent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(
            method = "setFrozenTicks",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelFrozenTicks(int frozenTicks, CallbackInfo ci) {
        Entity instance = (Entity) (Object) this;
        if (instance instanceof LivingEntity livingEntity && livingEntity.thermoo$getTemperature() > 0) {
            ci.cancel();
        }
    }


}
