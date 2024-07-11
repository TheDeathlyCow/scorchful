package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {

    @ModifyReturnValue(
            method = "getDefaultStack",
            at = @At("TAIL")
    )
    private ItemStack modifyDefaultStack(ItemStack original) {
        return ScorchfulItemEvents.GET_DEFAULT_STACK.invoker().onCreate(original);
    }


}
