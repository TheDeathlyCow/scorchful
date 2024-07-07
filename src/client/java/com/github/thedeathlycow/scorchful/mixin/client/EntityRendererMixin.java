package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(
            method = "shouldRender",
            at = @At("HEAD"),
            cancellable = true
    )
    private void shouldRender(
            T entity,
            Frustum frustum,
            double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir
    ) {
        var component = ScorchfulComponents.ENTITY_DESERT_VISION.get(entity);
        PlayerEntity mainPlayer = MinecraftClient.getInstance().player;
        if (mainPlayer != null && !mainPlayer.equals(component.getCause())) {
            cir.setReturnValue(false);
        }
    }

}
