package com.github.thedeathlycow.scorchful.event;

import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionDefinition;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

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

    void onActivated(RegistryEntry<HeatVisionDefinition> vision, ServerWorld world, BlockPos pos, PlayerEntity cause);
}
