package com.github.thedeathlycow.scorchful.mixin.client.entity.feature;

import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class HeadFeatureRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> {
    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelRenderIfSunHat(
            PoseStack matrices,
            SubmitNodeCollector queue,
            int light,
            S state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci
    ) {
        if (((SLivingEntityRenderState) state).scorchful$hasSunHat()) {
            ci.cancel();
        }
    }
}
