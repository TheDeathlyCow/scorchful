package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.client.HeatStrokeEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    private ClientWorld world;


    @Inject(
            method = "onEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectAdded(EntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        HeatStrokeEffects.onEffectAdded(packet, this.world, this.client);
    }

    @Inject(
            method = "onRemoveEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        HeatStrokeEffects.onEffectRemoved(packet, this.world, this.client);
    }

    @Inject(
            method = "onPlayerRespawn",
            at = @At("TAIL")
    )
    private void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        HeatStrokeEffects.onPlayerRespawn(packet, this.world, this.client);
    }

}
