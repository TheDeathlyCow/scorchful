package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConsumeItemTrigger.class)
public class ConsumeItemCriterionMixin {
    @Inject(
            method = "trigger",
            at = @At("HEAD")
    )
    private void onTriggered(ServerPlayer player, ItemStack stack, CallbackInfo ci) {
        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(stack, player);
    }
}
