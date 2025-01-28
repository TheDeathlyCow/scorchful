package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("isBeingRainedOn")
    boolean scorchful$invokeIsBeingRainedOn();
}
