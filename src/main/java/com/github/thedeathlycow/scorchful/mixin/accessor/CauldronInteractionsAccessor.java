package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CauldronInteractions.class)
public interface CauldronInteractionsAccessor {
    @Invoker("newDispatcher")
    static CauldronInteraction.Dispatcher scorchful$newDispatcher(final String name) {
        throw new IllegalStateException("Invoker");
    }
}