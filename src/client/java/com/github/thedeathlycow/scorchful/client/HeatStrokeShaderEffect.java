package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;

@Environment(EnvType.CLIENT)
public final class HeatStrokeShaderEffect {

    public static final HeatStrokeShaderEffect INSTANCE = new HeatStrokeShaderEffect();

    private boolean enabled = false;

    private static final ManagedShaderEffect HEAT_STROKE_SHADER = ShaderEffectManager.getInstance()
            .manage(Scorchful.id("shaders/post/heat_stroke.json"));

    public void initialize() {
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (enabled) {
                HEAT_STROKE_SHADER.render(tickDelta);
            }
        });
    }

    public void onEffectAdded(EntityStatusEffectS2CPacket packet, ClientWorld world, MinecraftClient client) {
        if (packet.getEffectId() == SStatusEffects.HEAT_STROKE && this.isEnabledByConfig()) {
            Entity entity = world.getEntityById(packet.getEntityId());
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                enabled = true;
            }
        }
    }

    public void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, ClientWorld world, MinecraftClient client) {
        if (packet.getEffectType() == SStatusEffects.HEAT_STROKE) {
            Entity entity = packet.getEntity(world);
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                enabled = false;
            }
        }
    }

    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, ClientWorld world, MinecraftClient client) {
        enabled = false;
    }

    private boolean isEnabledByConfig() {
        return Scorchful.getConfig().clientConfig.enableHeatStrokePostProcessing();
    }

    private HeatStrokeShaderEffect() {

    }

}
