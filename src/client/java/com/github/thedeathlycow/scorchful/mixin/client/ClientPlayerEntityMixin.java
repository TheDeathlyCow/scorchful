package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(
            method = "isAutoJumpEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private void autoJumpIfMesmerized(CallbackInfoReturnable<Boolean> cir) {
        if (ScorchfulComponents.MESMERIZED.get(this).isMesmerized()) {
            cir.setReturnValue(true);
        }
    }

}
