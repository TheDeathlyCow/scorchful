package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.temperature.heatvision.VisionSpawner;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin {

    @Inject(
            method = "turtleHelmetTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scorchfulTurtleArmorUpdate(CallbackInfo ci) {
        TurtleArmorEffects.update((Player) (Object) this);
        ci.cancel();
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void afterTick(CallbackInfo ci) {
        VisionSpawner.tick((Player) (Object) this);
    }

    @WrapMethod(
            method = "createAttributes"
    )
    private static AttributeSupplier.Builder appendAttributes(Operation<AttributeSupplier.Builder> original) {
        AttributeSupplier.Builder builder = original.call();
        builder.add(SEntityAttributes.REHYDRATION_EFFICIENCY);
        return builder;
    }
}
