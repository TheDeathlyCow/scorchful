package com.github.thedeathlycow.scorchful.mixin.compat.frostiful.absent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract int getTicksFrozen();

    @Inject(
            method = "setTicksFrozen",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelFrozenTicks(int frozenTicks, CallbackInfo ci) {
        Entity instance = (Entity) (Object) this;
        if (frozenTicks > this.getTicksFrozen()
                && instance instanceof LivingEntity livingEntity
                && livingEntity.thermoo$getTemperature() > 0) {
            ci.cancel();
        }
    }


}
