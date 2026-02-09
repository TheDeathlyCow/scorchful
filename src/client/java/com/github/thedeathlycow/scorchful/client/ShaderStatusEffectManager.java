package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.config.ScorchfulClientConfig;
import com.github.thedeathlycow.scorchful.config.section.AccessibilitySettings;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import java.util.function.Predicate;

public final class ShaderStatusEffectManager implements ShaderEffectRenderCallback, ClientPlayConnectionEvents.Disconnect {

//    private final ManagedShaderEffect managedShaderEffect;

    private final Identifier shaderID;

    private final Holder<MobEffect> potionEffect;

    private final Predicate<AccessibilitySettings> enabledPredicate;

    private boolean enabled = false;

    public ShaderStatusEffectManager(
//            ManagedShaderEffect managedShaderEffect,
            Identifier shaderID,
            Holder<MobEffect> potionEffect,
            Predicate<AccessibilitySettings> enabledPredicate
    ) {
//        this.managedShaderEffect = managedShaderEffect;
        this.shaderID = shaderID;
        this.potionEffect = potionEffect;
        this.enabledPredicate = enabledPredicate;
    }

    public void onEffectAdded(Holder<MobEffect> addedEffect) {
        if (addedEffect == potionEffect && this.enabledPredicate.test(ScorchfulClientConfig.getAccessibilitySettings())) {
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
    public void renderShaderEffects(Minecraft client, CrossFrameResourcePool pool, DeltaTracker tickDelta) {
        if (enabled) {
//            this.managedShaderEffect.render(tickDelta);
            PostChain postEffectProcessor = client.getShaderManager().getPostChain(this.shaderID, LevelTargetBundle.MAIN_TARGETS);
            if (postEffectProcessor != null) {
                postEffectProcessor.process(client.getMainRenderTarget(), pool);
            }
        }
    }
}
