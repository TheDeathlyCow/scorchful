package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ShaderStatusEffectManagers {

    private static final List<ShaderStatusEffectManager> TRACKED_MANAGERS = new ArrayList<>(2);

    private static final ShaderStatusEffectManager HEAT_STROKE = createAndTrack(
            Scorchful.id("shaders/post/heat_stroke.json"),
            SStatusEffects.HEAT_STROKE,
            ClientConfig::enableHeatStrokePostProcessing
    );

    private static final ShaderStatusEffectManager FEAR = createAndTrack(
            Scorchful.id("shaders/post/fear.json"),
            SStatusEffects.FEAR,
            ClientConfig::enableFearPostProcessing
    );

    public static void initialize() {
        ShaderStatusEffectManagers.getTrackedManagers().forEach(manager -> {
            ShaderEffectRenderCallback.EVENT.register(manager);
            ClientPlayConnectionEvents.DISCONNECT.register(manager);
        });
    }

    public static List<ShaderStatusEffectManager> getTrackedManagers() {
        return TRACKED_MANAGERS;
    }

    public static void onEffectAdded(ClientboundUpdateMobEffectPacket packet, ClientLevel world) {
        Entity entity = world.getEntity(packet.getEntityId());
        if (entity instanceof LocalPlayer player && player.isLocalPlayer()) {
            Holder<MobEffect> potionEffect = packet.getEffect();
            getTrackedManagers().forEach(manager -> manager.onEffectAdded(potionEffect));
        }
    }

    public static void onEffectRemoved(ClientboundRemoveMobEffectPacket packet, ClientLevel world) {
        Entity entity = packet.getEntity(world);
        if (entity instanceof LocalPlayer player && player.isLocalPlayer()) {
            Holder<MobEffect> potionEffect = packet.effect();
            getTrackedManagers().forEach(manager -> manager.onEffectRemoved(potionEffect));
        }
    }

    public static void onPlayerRespawn() {
        ShaderStatusEffectManagers.getTrackedManagers().forEach(ShaderStatusEffectManager::onPlayerRespawn);
    }

    public static ShaderStatusEffectManager createAndTrack(
            ResourceLocation shaderID,
            Holder<MobEffect> potionEffect,
            Predicate<ClientConfig> enabledPredicate
    ) {
        ManagedShaderEffect managedShaderEffect = ShaderEffectManager.getInstance().manage(shaderID);
        var statusEffectShader = new ShaderStatusEffectManager(
                managedShaderEffect,
                potionEffect,
                enabledPredicate
        );
        TRACKED_MANAGERS.add(statusEffectShader);
        return statusEffectShader;
    }

    private ShaderStatusEffectManagers() {

    }
}
