package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.Builder.class)
public interface EnchantmentBuilderAccessor {
    @Accessor("definition")
    Enchantment.Definition getDefinition();
}
