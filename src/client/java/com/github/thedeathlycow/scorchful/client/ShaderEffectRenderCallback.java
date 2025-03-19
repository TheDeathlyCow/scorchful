package com.github.thedeathlycow.scorchful.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;

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

    void renderShaderEffects(MinecraftClient client, Pool pool, RenderTickCounter tickCounter);
}