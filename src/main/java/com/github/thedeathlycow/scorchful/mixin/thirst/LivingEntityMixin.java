package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.item.QuenchingFoods;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(
            method = "applyFoodEffects",
            at = @At("HEAD")
    )
    private void scorchful_onFoodEaten(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        QuenchingFoods.onConsume(targetEntity, stack);
    }

}
