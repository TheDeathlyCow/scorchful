package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.item.QuenchingFoods;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConsumeItemCriterion.class)
public class ConsumeItemCriterionMixin {
    @Inject(
            method = "trigger",
            at = @At("HEAD")
    )
    private void onItemConsumed(ServerPlayerEntity player, ItemStack stack, CallbackInfo ci) {
        QuenchingFoods.onConsume(player, stack);
    }
}
