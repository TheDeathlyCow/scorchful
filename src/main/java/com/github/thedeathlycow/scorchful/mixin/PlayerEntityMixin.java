package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.temperature.heatvision.VisionSpawner;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(
            method = "updateTurtleHelmet",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scorchfulTurtleArmorUpdate(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void afterTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        VisionSpawner.tick(player);
        TurtleArmorEffects.update(player);
    }

    @Inject(
            method = "createPlayerAttributes",
            at = @At("TAIL")
    )
    private static void appendAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
        builder.add(SEntityAttributes.REHYDRATION_EFFICIENCY);
        builder.add(SEntityAttributes.LUNG_CAPACITY);
    }

}
