package com.github.thedeathlycow.scorchful.mixin.accessor;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CauldronInteraction.Dispatcher.class)
public interface CauldronInteractionDispatcherAccessor {
    @Invoker("put")
    void scorchful$put(final Item item, final CauldronInteraction interaction);

    @Invoker("put")
    void scorchful$put(final TagKey<Item> tag, final CauldronInteraction interaction);
}