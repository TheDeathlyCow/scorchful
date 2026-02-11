package com.github.thedeathlycow.scorchful.client;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

/**
 * A port of the same event from Satin API, distributed under LGPLv3. This is not meant to be stable; prefer Satin when it comes out
 */
@Deprecated
public interface ShaderEffectRenderCallback {
    /**
     * Fired when Minecraft renders the entity outline framebuffer.
     * Post process shader effects should generally be rendered at that time.
     */
    Event<ShaderEffectRenderCallback> EVENT = EventFactory.createArrayBacked(
            ShaderEffectRenderCallback.class,
            listeners -> (client, pool, tickCounter) -> {
                for (ShaderEffectRenderCallback listener : listeners) {
                    listener.renderShaderEffects(client, pool, tickCounter);
                }
            }
    );

    void renderShaderEffects(Minecraft client, CrossFrameResourcePool pool, DeltaTracker tickCounter);
}