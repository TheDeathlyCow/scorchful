package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.serialization.DataResult;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(
            method = "validateComponents",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void validateDrinkContainer(ComponentMap components, CallbackInfoReturnable<DataResult<Unit>> cir) {
        if (components.contains(SDataComponentTypes.DRINK_CONTAINER) && components.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 1) > 1) {
            cir.setReturnValue(DataResult.error(() -> "Item cannot be both a drink container and stackable"));
        }
    }
}
