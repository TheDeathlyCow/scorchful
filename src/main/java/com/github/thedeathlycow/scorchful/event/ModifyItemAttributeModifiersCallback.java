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
            listeners -> (stack, slot, builder) -> {
                for (ModifyItemAttributeModifiersCallback listener : listeners) {
                    listener.modifyAttributeModifiers(stack, slot, builder);
                }
            }
    );

    void modifyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, AttributeModifiersComponent.Builder builder);

    @ApiStatus.Internal
    static AttributeModifiersComponent invoke(ItemStack stack, AttributeModifierSlot slot, AttributeModifiersComponent base) {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();

        for (AttributeModifiersComponent.Entry entry : base.modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(stack, slot, builder);
        return new AttributeModifiersComponent(builder.build().modifiers(), base.showInTooltip());
    }
}