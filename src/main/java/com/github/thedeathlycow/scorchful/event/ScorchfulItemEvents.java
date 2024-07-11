package com.github.thedeathlycow.scorchful.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ScorchfulItemEvents {

    public static final Event<CreateStack> GET_DEFAULT_STACK = EventFactory.createArrayBacked(
            CreateStack.class,
            listeners -> stack -> {
                for (CreateStack listener : listeners) {
                    stack = listener.onCreate(stack);
                }
                return stack;
            }
    );

    @FunctionalInterface
    public interface CreateStack {

        ItemStack onCreate(ItemStack stack);

    }

}
