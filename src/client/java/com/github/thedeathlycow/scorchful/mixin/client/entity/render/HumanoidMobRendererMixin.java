package com.github.thedeathlycow.scorchful.mixin.client.entity.render;

import com.github.thedeathlycow.scorchful.entity.state.SLivingEntityRenderState;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    @Inject(
            method = "extractHumanoidRenderState",
            at = @At("TAIL")
    )
    private static void updateExtendedState(LivingEntity entity, HumanoidRenderState state, float tickDelta, ItemModelResolver itemModelResolver, CallbackInfo ci){
        SLivingEntityRenderState sState = (SLivingEntityRenderState) state;

        sState.scorchful$hasSunHat(entity.getItemBySlot(EquipmentSlot.HEAD).has(SDataComponentTypes.SUN_HAT_RENDERER));
    }
}