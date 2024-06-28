package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.client.HeatStrokeShaderEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    @Shadow
    private ClientWorld world;

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }


    @Inject(
            method = "onEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectAdded(EntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        HeatStrokeShaderEffect.INSTANCE.onEffectAdded(packet, this.world, this.client);
    }

    @Inject(
            method = "onRemoveEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        HeatStrokeShaderEffect.INSTANCE.onEffectRemoved(packet, this.world, this.client);
    }

    @Inject(
            method = "onPlayerRespawn",
            at = @At("TAIL")
    )
    private void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        HeatStrokeShaderEffect.INSTANCE.onPlayerRespawn(packet, this.world, this.client);
    }

}
