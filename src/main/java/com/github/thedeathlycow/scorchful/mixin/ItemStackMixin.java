package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/MergedComponentMap;)V",
            at = @At("TAIL")
    )
    private void modifyDefaultStack(ItemConvertible item, int count, MergedComponentMap components, CallbackInfo ci) {
        ItemStack original = (ItemStack) (Object) this;
        ScorchfulItemEvents.GET_DEFAULT_STACK.invoker().onCreate(original);
    }
}
