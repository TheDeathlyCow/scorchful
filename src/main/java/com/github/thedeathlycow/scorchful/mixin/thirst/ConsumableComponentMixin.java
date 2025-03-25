package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ConsumableComponent.class)
public class ConsumableComponentMixin {
    @WrapMethod(
            method = "canConsume"
    )
    private boolean blockConsumptionIfContainerIsEmpty(LivingEntity user, ItemStack stack, Operation<Boolean> original) {
        DrinkContainerComponent container = stack.get(SDataComponentTypes.DRINK_CONTAINER);
        if (container != null && container.isEmpty()) {
            return false;
        }

        return original.call(user, stack);
    }
}