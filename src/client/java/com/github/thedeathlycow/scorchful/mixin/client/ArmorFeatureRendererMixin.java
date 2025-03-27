package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @WrapMethod(method = "hasModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;)Z")
    private static boolean hasSunHatOrModel(ItemStack stack, EquipmentSlot slot, Operation<Boolean> original) {
        return stack.contains(SDataComponentTypes.HAS_SUN_HAT_MODEL) || original.call(stack, slot);
    }
}