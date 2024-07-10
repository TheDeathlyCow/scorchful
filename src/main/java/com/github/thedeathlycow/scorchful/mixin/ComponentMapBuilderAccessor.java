package com.github.thedeathlycow.scorchful.mixin;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ComponentMap.Builder.class)
public interface ComponentMapBuilderAccessor {

    @Accessor("components")
    Reference2ObjectMap<ComponentType<?>, Object> scorchful$components();

}
