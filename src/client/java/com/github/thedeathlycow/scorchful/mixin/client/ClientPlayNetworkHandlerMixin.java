package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "onEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectAdded(EntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        if (packet.getEffectId() == SStatusEffects.HEAT_STROKE) {
            Entity entity = this.world.getEntityById(packet.getEntityId());
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                ((GameRendererAccessor) this.client.gameRenderer).invokeLoadPostProcessor(
                        Scorchful.id("shaders/post/heat_stroke.json")
                );
            }
        }
    }

    @Inject(
            method = "onRemoveEntityStatusEffect",
            at = @At("TAIL")
    )
    private void onEffectRemoved(RemoveEntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        if (packet.getEffectType() == SStatusEffects.HEAT_STROKE) {
            Entity entity = packet.getEntity(this.world);
            if (entity instanceof ClientPlayerEntity player && player.isMainPlayer()) {
                this.client.gameRenderer.disablePostProcessor();
            }
        }
    }

}
