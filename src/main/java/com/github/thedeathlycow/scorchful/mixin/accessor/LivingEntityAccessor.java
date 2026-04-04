package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("decreaseAirSupply")
    int scorchfulInvokeDecreaseAirSupply(final int currentSupply);

    @Invoker("shouldTakeDrowningDamage")
    boolean scorchfulInvokeShouldTakeDrowningDamage();
}
