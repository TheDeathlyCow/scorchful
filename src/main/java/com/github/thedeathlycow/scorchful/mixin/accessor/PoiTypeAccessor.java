package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PoiType.class)
public interface PoiTypeAccessor {
    @Accessor("matchingStates")
    @Mutable
    void scorchful$setBlockStates(Set<BlockState> states);
}