package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.config.ThirstConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class ConsumePotionMixin {

    @Inject(
            method = "finishUsing",
            at = @At("HEAD")
    )
    private void scorchful_onPotionConsumed(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user.isPlayer()) {
            ThirstConfig config = Scorchful.getConfig().thirstConfig;
            ScorchfulComponents.PLAYER.get(user)
                    .addWater(config.getWaterFromDrinking());
        }
    }

}
