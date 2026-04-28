package com.github.thedeathlycow.scorchful.item;

import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SingleDrinkItem extends DrinkItem {

    private final Supplier<ItemStack> postConsumeItem;

    public SingleDrinkItem(Properties settings, Supplier<ItemStack> postConsumeItem) {
        super(settings);
        this.postConsumeItem = postConsumeItem;
    }

    @Override
    protected ItemStack getPostConsumeStack(ItemStack stack, Level world, ServerPlayer serverPlayer) {
        if (!serverPlayer.isCreative()) {
            stack.shrink(1);
            ItemStack result = this.postConsumeItem.get();
            if (stack.isEmpty()) {
                return result;
            }
            serverPlayer.getInventory().add(result);
        }
        return stack;
    }
}
