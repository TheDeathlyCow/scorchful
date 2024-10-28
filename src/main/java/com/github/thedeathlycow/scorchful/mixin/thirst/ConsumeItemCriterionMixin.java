package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.DrinkItem;
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
    private void onTriggered(ServerPlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(stack, player);
    }


}
