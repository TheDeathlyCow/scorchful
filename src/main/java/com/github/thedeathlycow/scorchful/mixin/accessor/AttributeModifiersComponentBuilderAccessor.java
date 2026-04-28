package com.github.thedeathlycow.scorchful.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemAttributeModifiers.Builder.class)
public interface AttributeModifiersComponentBuilderAccessor {
    @Accessor("entries")
    ImmutableList.Builder<ItemAttributeModifiers.Entry> scorchful$getEntries();
}