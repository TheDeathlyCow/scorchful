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

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(
            method = "turtleHelmetTick",
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
        Player player = (Player) (Object) this;
        VisionSpawner.tick(player);
        TurtleArmorEffects.update(player);
    }

    @WrapMethod(
            method = "createAttributes"
    )
    private static AttributeSupplier.Builder registerAttributes(Operation<AttributeSupplier.Builder> original) {
        AttributeSupplier.Builder builder = original.call();
        builder.add(SEntityAttributes.REHYDRATION_EFFICIENCY);
        builder.add(SEntityAttributes.LUNG_CAPACITY);
        return builder;
    }
}
