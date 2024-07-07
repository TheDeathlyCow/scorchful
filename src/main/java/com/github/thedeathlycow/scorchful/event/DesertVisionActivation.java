package com.github.thedeathlycow.scorchful.event;

import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

@FunctionalInterface
public interface DesertVisionActivation {

    /**
     * Fired when a player approaches a desert vision.
     */
    Event<DesertVisionActivation> EVENT = EventFactory.createArrayBacked(
            DesertVisionActivation.class,
            listeners -> (vision, player) -> {
                for (var listener : listeners) {
                    listener.onActivated(vision, player);
                }
            }
    );

    void onActivated(HeatVision vision, PlayerEntity player);
}
