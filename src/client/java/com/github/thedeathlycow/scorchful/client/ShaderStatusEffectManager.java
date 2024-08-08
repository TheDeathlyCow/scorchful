package com.github.thedeathlycow.scorchful.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;

public final class ShaderStatusEffectManager implements ShaderEffectRenderCallback, ClientPlayConnectionEvents.Disconnect {

    private final ManagedShaderEffect managedShaderEffect;

    private final RegistryEntry<StatusEffect> potionEffect;

    private boolean enabled = false;

    public ShaderStatusEffectManager(ManagedShaderEffect managedShaderEffect, RegistryEntry<StatusEffect> potionEffect) {
        this.managedShaderEffect = managedShaderEffect;
        this.potionEffect = potionEffect;
    }

    public void onEffectAdded(RegistryEntry<StatusEffect> addedEffect) {
        if (addedEffect == potionEffect) {
            enabled = true;
        }
    }

    public void onEffectRemoved(RegistryEntry<StatusEffect> removedEffect) {
        if (removedEffect == potionEffect) {
            enabled = false;
        }
    }
    public void onPlayerRespawn() {
        enabled = false;
    }

    @Override
    public void renderShaderEffects(float tickDelta) {
        if (enabled) {
            this.managedShaderEffect.render(tickDelta);
        }
    }

    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        this.enabled = false;
    }
}
