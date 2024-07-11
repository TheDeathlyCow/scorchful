package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.BlockDisplayEntity.class)
public interface BlockDisplayAccess {

    @Invoker("setBlockState")
    void scorchful$setBlockState(BlockState state);

}
