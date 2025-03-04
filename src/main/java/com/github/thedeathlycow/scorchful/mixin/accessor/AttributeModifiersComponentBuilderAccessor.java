package com.github.thedeathlycow.scorchful.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.type.AttributeModifiersComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeModifiersComponent.Builder.class)
public interface AttributeModifiersComponentBuilderAccessor {
    @Accessor("entries")
    ImmutableList.Builder<AttributeModifiersComponent.Entry> scorchful$getEntries();
}