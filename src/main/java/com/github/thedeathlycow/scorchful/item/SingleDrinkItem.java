package com.github.thedeathlycow.scorchful.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SingleDrinkItem extends DrinkItem {

    private final Supplier<ItemStack> postConsumeItem;

    public SingleDrinkItem(Settings settings, Supplier<ItemStack> postConsumeItem) {
        super(settings);
        this.postConsumeItem = postConsumeItem;
    }

    @Override
    protected ItemStack getPostConsumeStack(ItemStack stack, World world, ServerPlayerEntity serverPlayer) {
        if (!serverPlayer.isCreative()) {
            stack.decrement(1);
            ItemStack result = this.postConsumeItem.get();
            if (stack.isEmpty()) {
                return result;
            }
            serverPlayer.getInventory().insertStack(result);
        }
        return stack;
    }
}
