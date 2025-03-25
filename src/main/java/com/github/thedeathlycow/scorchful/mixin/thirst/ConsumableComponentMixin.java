package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @WrapOperation(
            method = "finishConsumption",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"
            )
    )
    private void blockContainerDeletion(ItemStack instance, int amount, LivingEntity entity, Operation<Void> original) {
        // we don't want to remove the item when it has a drink container, instead decrement drink count with below method
        if (!instance.contains(SDataComponentTypes.DRINK_CONTAINER)) {
            original.call(instance, amount, entity);
        }
    }

    @Inject(
            method = "finishConsumption",
            at = @At("TAIL")
    )
    private void decrementContainer(World world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        DrinkContainerComponent component = stack.get(SDataComponentTypes.DRINK_CONTAINER);
        if (component != null && component.hasDrink()) {
            stack.set(SDataComponentTypes.DRINK_CONTAINER, component.addDrinks(-1));
        }
    }
}