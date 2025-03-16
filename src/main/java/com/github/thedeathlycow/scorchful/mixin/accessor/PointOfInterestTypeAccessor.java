package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeAccessor {
    @Accessor("blockStates")
    @Mutable
    void scorchful$setBlockStates(Set<BlockState> states);
}