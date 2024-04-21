package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.ScorchfulClient;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.mixin.client.GameRendererAccessor;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HeatStrokeEffects {

    private static final Identifier HEAT_STROKE_SHADER_ID = Scorchful.id("shaders/post/heat_stroke.json");

    public static void onEffectAdded(EntityStatusEffectS2CPacket packet, ClientWorld world, MinecraftClient client) {

        ScorchfulConfig config = Scorchful.getConfig();
        if (client.gameRenderer.getPostProcessor() != null || !config.clientConfig.enableHeatStrokePostProcessing()) {
            return;
        }

        if (packet.getEffectId() == SStatusEffects.HEAT_STROKE) {
            Entity entity = world.getEntityById(packet.getEntityId());
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                ((GameRendererAccessor) client.gameRenderer)
                        .invokeLoadPostProcessor(HEAT_STROKE_SHADER_ID);
            }
        }
    }

    public static void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, ClientWorld world, MinecraftClient client) {
        if (packet.getEffectType() == SStatusEffects.HEAT_STROKE) {
            Entity entity = packet.getEntity(world);
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                client.gameRenderer.disablePostProcessor();
            }
        }
    }

    public static void onPlayerRespawn(PlayerRespawnS2CPacket packet, ClientWorld world, MinecraftClient client) {
        ScorchfulConfig config = Scorchful.getConfig();
        if (client.gameRenderer.getPostProcessor() != null && config.clientConfig.enableHeatStrokePostProcessing()) {
            client.gameRenderer.disablePostProcessor();
        }
    }

    private HeatStrokeEffects() {

    }

}
