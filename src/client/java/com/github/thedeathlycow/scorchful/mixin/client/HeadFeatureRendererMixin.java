package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.registry.SItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelRenderIfSunHat(
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int i,
            T livingEntity,
            float f, float g, float h,
            float j, float k, float l,
            CallbackInfo ci
    ) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.is(SItems.SUN_HAT)) {
            ci.cancel();
        }
    }


}
