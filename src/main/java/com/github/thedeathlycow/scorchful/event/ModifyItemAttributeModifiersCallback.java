package com.github.thedeathlycow.scorchful.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
public interface ModifyItemAttributeModifiersCallback {
    Event<ModifyItemAttributeModifiersCallback> EVENT = EventFactory.createArrayBacked(
            ModifyItemAttributeModifiersCallback.class,
            listeners -> (stack, slot, component) -> {
                for (ModifyItemAttributeModifiersCallback listener : listeners) {
                    component = listener.modifyAttributeModifiers(stack, slot, component);
                }
                return component;
            }
    );

    AttributeModifiersComponent modifyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, AttributeModifiersComponent component);

    @ApiStatus.Internal
    static AttributeModifiersComponent invoke(ItemStack stack, AttributeModifierSlot slot, AttributeModifiersComponent base) {
        return ModifyItemAttributeModifiersCallback.EVENT.invoker()
                .modifyAttributeModifiers(stack, slot, base);
    }
}