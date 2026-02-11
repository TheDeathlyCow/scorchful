package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(
            method = "validateComponents",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void validateDrinkContainer(DataComponentMap components, CallbackInfoReturnable<DataResult<Unit>> cir) {
        if (components.has(SDataComponentTypes.DRINK_CONTAINER) && components.getOrDefault(DataComponents.MAX_STACK_SIZE, 1) > 1) {
            cir.setReturnValue(DataResult.error(() -> "Item cannot be both a drink container and stackable"));
        }
    }
}
