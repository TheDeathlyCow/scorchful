package com.github.thedeathlycow.scorchful.mixin.thirst;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("isBeingRainedOn")
    boolean scorchful_invokeIsBeingRainedOn();

    @Invoker("isInsideBubbleColumn")
    boolean scorchful_invokeIsInsideBubbleColumn();

}