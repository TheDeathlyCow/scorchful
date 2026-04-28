package com.github.thedeathlycow.scorchful.event;

import com.github.thedeathlycow.scorchful.temperature.heatvision.HeatVision;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface HeatVisionActivation {

    /**
     * Fired when a player approaches a heat vision.
     */
    Event<HeatVisionActivation> EVENT = EventFactory.createArrayBacked(
            HeatVisionActivation.class,
            listeners -> (vision, world, pos, cause) -> {
                for (var listener : listeners) {
                    listener.onActivated(vision, world, pos, cause);
                }
            }
    );

    void onActivated(HeatVision vision, ServerLevel world, BlockPos pos, Player cause);
}
