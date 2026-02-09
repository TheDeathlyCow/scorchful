package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
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
            method = "onConsume",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void blockContainerDeletion(ItemStack instance, int amount, LivingEntity entity, Operation<Void> original) {
        // we don't want to remove the item when it has a drink container, instead decrement drink count with below method
        if (!instance.has(SDataComponentTypes.DRINK_CONTAINER)) {
            original.call(instance, amount, entity);
        }
    }

    @Inject(
            method = "onConsume",
            at = @At("TAIL")
    )
    private void decrementContainer(Level world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        DrinkContainerComponent component = stack.get(SDataComponentTypes.DRINK_CONTAINER);
        if (component != null && component.hasDrink()) {
            stack.set(SDataComponentTypes.DRINK_CONTAINER, component.addDrinks(-1));
        }
    }

    @Inject(
            method = "emitParticlesAndSounds",
            at = @At("TAIL")
    )
    private void spawnWaterParticles(RandomSource random, LivingEntity user, ItemStack stack, int particleCount, CallbackInfo ci) {
        DrinkLevelComponent level = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (level == DrinkLevelComponent.HYDRATING) {
            DrinkLevelComponent.spawnWaterParticles(user.level(), user, particleCount);
        }
    }
}