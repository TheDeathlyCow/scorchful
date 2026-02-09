package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(Holder.Reference.class)
public interface HolderReferenceAccessor {
    @Accessor("tags")
    Set<TagKey<?>> scorchful$tags();
}