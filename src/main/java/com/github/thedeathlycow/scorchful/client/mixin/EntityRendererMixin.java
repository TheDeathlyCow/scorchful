package com.github.thedeathlycow.scorchful.client.mixin;

import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
        var component = entity.getData(ScorchfulEntityAttachments.ENTITY_DESERT_VISION);
        if (component.hasDesertVision()) {
            Player mainPlayer = Minecraft.getInstance().player;
            cir.setReturnValue(mainPlayer == null || mainPlayer.equals(component.getCause()));
        }
    }

}
