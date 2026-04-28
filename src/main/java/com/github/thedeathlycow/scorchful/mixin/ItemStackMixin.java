package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
            at = @At("TAIL")
    )
    private void modifyDefaultStack(ItemLike item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        ItemStack original = (ItemStack) (Object) this;
        ScorchfulItemEvents.GET_DEFAULT_STACK.invoker().onCreate(original);
    }
}
