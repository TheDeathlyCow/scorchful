package com.github.thedeathlycow.scorchful.mixin.client.entity.render;

import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityRenderer.class)
public class BipedEntityRendererMixin {
    @Inject(
            method = "updateBipedRenderState",
            at = @At("TAIL")
    )
    private static void updateExtendedState(LivingEntity entity, BipedEntityRenderState state, float tickDelta, ItemModelManager itemModelResolver, CallbackInfo ci){
        SLivingEntityRenderState sState = (SLivingEntityRenderState) state;

        sState.scorchful$hasSunHat(entity.getEquippedStack(EquipmentSlot.HEAD).contains(SDataComponentTypes.SUN_HAT_RENDERER));
    }
}