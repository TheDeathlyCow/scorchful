package com.github.thedeathlycow.scorchful.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class ScorchfulItemEvents {

    public static final Event<CreateStack> GET_DEFAULT_STACK = EventFactory.createArrayBacked(
            CreateStack.class,
            listeners -> stack -> {
                for (CreateStack listener : listeners) {
                    listener.onCreate(stack);
                }
            }
    );

    public static final Event<ConsumeItemCallback> CONSUME_ITEM = EventFactory.createArrayBacked(
            ConsumeItemCallback.class,
            listeners -> (stack, player) -> {
                for (ConsumeItemCallback listener : listeners) {
                    listener.consume(stack, player);
                }
            }
    );

    @FunctionalInterface
    public interface CreateStack {
        void onCreate(ItemStack stack);
    }

    @FunctionalInterface
    public interface ConsumeItemCallback {
        void consume(ItemStack stack, ServerPlayerEntity player);
    }
}
