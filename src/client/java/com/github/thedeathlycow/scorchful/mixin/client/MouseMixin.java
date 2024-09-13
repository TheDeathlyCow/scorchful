package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin {

    @WrapOperation(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
            )
    )
    private void lockMouseWhenMesmerized(ClientPlayerEntity instance, double cursorDeltaX, double cursorDeltaY, Operation<Void> original) {
        if (!ScorchfulComponents.MESMERIZED.get(instance).isMesmerized()) {
            original.call(instance, cursorDeltaX, cursorDeltaY);
        }
    }

}
