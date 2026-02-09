package com.github.thedeathlycow.scorchful.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

@Mixin(Holder.Reference.class)
public interface RegistryEntryReferenceAccessor {
    @Accessor("tags")
    Set<TagKey<?>> scorchful$tags();
}