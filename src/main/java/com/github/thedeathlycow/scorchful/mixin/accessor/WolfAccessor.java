package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Wolf.class)
public interface WolfAccessor {
    @Invoker("getSoundSet")
    WolfSoundVariant.WolfSoundSet scorchful$getSoundSet();
}
