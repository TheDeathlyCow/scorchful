package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class ConsumeFoodMixin {

    @Inject(
            method = "applyFoodEffects",
            at = @At("HEAD")
    )
    private void scorchful_onFoodEaten(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        if (stack.isIn(SItemTags.QUENCHING_FOODS) && targetEntity.isPlayer()) {
            HeatingConfig config = Scorchful.getConfig().heatingConfig;
            ScorchfulComponents.PLAYER.get(targetEntity)
                    .addWater(config.getWaterFromQuenchingFood());
        }
    }

}
