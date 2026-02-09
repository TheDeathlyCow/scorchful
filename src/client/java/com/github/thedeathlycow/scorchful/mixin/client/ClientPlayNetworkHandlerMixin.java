package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.client.ShaderStatusEffectManagers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl {
    @Shadow
    private ClientLevel level;

    protected ClientPlayNetworkHandlerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(
            method = "handleUpdateMobEffect",
            at = @At("TAIL")
    )
    private void onEffectAdded(ClientboundUpdateMobEffectPacket packet, CallbackInfo ci) {
        ShaderStatusEffectManagers.onEffectAdded(packet, this.level);
    }

    @Inject(
            method = "handleRemoveMobEffect",
            at = @At("TAIL")
    )
    private void onEffectRemoved(ClientboundRemoveMobEffectPacket packet, CallbackInfo ci) {
        ShaderStatusEffectManagers.onEffectRemoved(packet, this.level);
    }

    @Inject(
            method = "handleRespawn",
            at = @At("TAIL")
    )
    private void onPlayerRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        ShaderStatusEffectManagers.onPlayerRespawn();
    }
}
