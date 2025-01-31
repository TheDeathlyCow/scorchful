package com.github.thedeathlycow.scorchful.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Event that is invoked after the water skin item successfully collects water from a water source such as a water block,
 * cauldron, or warped lily.
 */
@FunctionalInterface
public interface CollectWaterCallback {
    Event<CollectWaterCallback> EVENT = EventFactory.createArrayBacked(
            CollectWaterCallback.class,
            listeners -> (user, stack, sourcePos) -> {
                for (CollectWaterCallback listener : listeners) {
                    listener.onWaterCollected(user, stack, sourcePos);
                }
            }
    );

    /**
     * @param user      The player collecting water
     * @param stack     The water skin item stack
     * @param sourcePos The block position of the collection source
     */
    void onWaterCollected(PlayerEntity user, ItemStack stack, BlockPos sourcePos);
}