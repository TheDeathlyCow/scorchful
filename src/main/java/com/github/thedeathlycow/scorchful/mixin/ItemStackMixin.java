package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V",
            at = @At("TAIL")
    )
    private void modifyComponentsByTag(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo ci) {
        ItemStack instance = (ItemStack) (Object) this;
        DrinkLevelComponent levelComponent = DrinkLevelComponent.byTag(instance);
        if (levelComponent != null) {
            components.set(SDataComponentTypes.DRINK_LEVEL, levelComponent);
        }
    }
}
