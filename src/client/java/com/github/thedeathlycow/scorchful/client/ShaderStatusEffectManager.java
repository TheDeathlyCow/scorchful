package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public final class ShaderStatusEffectManager implements ShaderEffectRenderCallback, ClientPlayConnectionEvents.Disconnect {

//    private final ManagedShaderEffect managedShaderEffect;

    private final Identifier shaderID;

    private final RegistryEntry<StatusEffect> potionEffect;

    private final Predicate<ClientConfig> enabledPredicate;

    private boolean enabled = false;

    public ShaderStatusEffectManager(
//            ManagedShaderEffect managedShaderEffect,
            Identifier shaderID,
            RegistryEntry<StatusEffect> potionEffect,
            Predicate<ClientConfig> enabledPredicate
    ) {
//        this.managedShaderEffect = managedShaderEffect;
        this.shaderID = shaderID;
        this.potionEffect = potionEffect;
        this.enabledPredicate = enabledPredicate;
    }

    public void onEffectAdded(RegistryEntry<StatusEffect> addedEffect) {
        if (addedEffect == potionEffect && this.enabledPredicate.test(Scorchful.getConfig().clientConfig)) {
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
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        this.enabled = false;
    }

    @Override
    public void renderShaderEffects(MinecraftClient client, Pool pool, RenderTickCounter tickDelta) {
        if (enabled) {
//            this.managedShaderEffect.render(tickDelta);
            PostEffectProcessor postEffectProcessor = client.getShaderLoader().loadPostEffect(this.shaderID, DefaultFramebufferSet.MAIN_ONLY);
            if (postEffectProcessor != null) {
                postEffectProcessor.render(client.getFramebuffer(), pool);
            }
        }
    }
}
