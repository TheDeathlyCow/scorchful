package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

import java.util.ArrayList;
import java.util.List;

public final class ShaderStatusEffectManagers {

    private static final List<ShaderStatusEffectManager> TRACKED_MANAGERS = new ArrayList<>(2);

    public static void initialize() {
        create(Scorchful.id("shaders/post/heat_stroke.json"), SStatusEffects.HEAT_STROKE);
//        create(Scorchful.id("shaders/post/fear.json"), SStatusEffects.FEAR);
        ShaderStatusEffectManagers.getTrackedManagers().forEach(manager -> {
            ShaderEffectRenderCallback.EVENT.register(manager);
            ClientPlayConnectionEvents.DISCONNECT.register(manager);
        });
    }

    public static List<ShaderStatusEffectManager> getTrackedManagers() {
        return TRACKED_MANAGERS;
    }

    public static void onEffectAdded(EntityStatusEffectS2CPacket packet, ClientWorld world) {
        Entity entity = world.getEntityById(packet.getEntityId());
        if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
            RegistryEntry<StatusEffect> potionEffect = packet.getEffectId();
            getTrackedManagers().forEach(manager -> manager.onEffectAdded(potionEffect));
        }
    }

    public static void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, ClientWorld world) {
        Entity entity = packet.getEntity(world);
        if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
            RegistryEntry<StatusEffect> potionEffect = packet.effect();
            getTrackedManagers().forEach(manager -> manager.onEffectRemoved(potionEffect));
        }
    }

    public static void onPlayerRespawn() {
        ShaderStatusEffectManagers.getTrackedManagers().forEach(ShaderStatusEffectManager::onPlayerRespawn);
    }

    public static ShaderStatusEffectManager create(Identifier shaderID, RegistryEntry<StatusEffect> potionEffect) {
        ManagedShaderEffect managedShaderEffect = ShaderEffectManager.getInstance().manage(shaderID);
        var statusEffectShader = new ShaderStatusEffectManager(managedShaderEffect, potionEffect);
        TRACKED_MANAGERS.add(statusEffectShader);
        return statusEffectShader;
    }

    private ShaderStatusEffectManagers() {

    }
}
