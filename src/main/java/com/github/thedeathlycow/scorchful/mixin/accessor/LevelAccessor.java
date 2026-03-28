package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(Level.class)
public interface LevelAccessor {
    @Invoker("getClockTimeTicks")
    long scorchful$getClockTimeTicks(final Optional<? extends Holder<WorldClock>> clock);
}