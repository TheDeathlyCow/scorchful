package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.BlockDisplay.class)
public interface BlockDisplayAccess {

    @Invoker("setBlockState")
    void scorchful$setBlockState(BlockState state);

}
