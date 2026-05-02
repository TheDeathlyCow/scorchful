package com.github.thedeathlycow.scorchful.client.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Predicate;

public final class ShaderStatusEffectManager implements ShaderEffectRenderCallback, ClientPlayConnectionEvents.Disconnect {
    private final ManagedShaderEffect managedShaderEffect;

    private final Holder<MobEffect> potionEffect;

    private final Predicate<ClientConfig> enabledPredicate;

    private boolean enabled = false;

    public ShaderStatusEffectManager(
            ManagedShaderEffect managedShaderEffect,
            Holder<MobEffect> potionEffect,
            Predicate<ClientConfig> enabledPredicate
    ) {
        this.managedShaderEffect = managedShaderEffect;
        this.potionEffect = potionEffect;
        this.enabledPredicate = enabledPredicate;
    }

    public void onEffectAdded(Holder<MobEffect> addedEffect) {
        if (addedEffect == potionEffect && this.enabledPredicate.test(Scorchful.getConfig().clientConfig)) {
            enabled = true;
        }
    }

    public void onEffectRemoved(Holder<MobEffect> removedEffect) {
        if (removedEffect == potionEffect) {
            enabled = false;
        }
    }

    public void onPlayerRespawn() {
        enabled = false;
    }

    @Override
    public void onPlayDisconnect(ClientPacketListener handler, Minecraft client) {
        this.enabled = false;
    }

    @Override
    public void renderShaderEffects(float tickDelta) {
        if (enabled) {
            this.managedShaderEffect.render(tickDelta);
        }
    }
}
