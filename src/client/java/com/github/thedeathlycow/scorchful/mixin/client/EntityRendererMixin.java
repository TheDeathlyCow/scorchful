package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
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
        var component = ScorchfulComponents.DESERT_VISION_CHILD.get(entity);
        if (component.isDesertVisionChild() && component.getCausingPlayerID() == null) {
            cir.setReturnValue(false);
        }
    }

}
