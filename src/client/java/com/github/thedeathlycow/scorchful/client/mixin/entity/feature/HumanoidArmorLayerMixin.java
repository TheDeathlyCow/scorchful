package com.github.thedeathlycow.scorchful.client.mixin.entity.feature;

import com.github.thedeathlycow.scorchful.item.component.SunHatRenderer;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @WrapMethod(method = "shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z")
    private static boolean hasSunHatOrModel(ItemStack stack, EquipmentSlot slot, Operation<Boolean> original) {
        SunHatRenderer renderer = stack.get(SDataComponentTypes.SUN_HAT_RENDERER);

        if (renderer != null && renderer.replaceArmorModel()) {
            return false;
        }

        return original.call(stack, slot);
    }
}