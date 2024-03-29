package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.item.TurtleArmorEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(
            method = "updateTurtleHelmet",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scorchfulTurtleArmorUpdate(CallbackInfo ci) {
        TurtleArmorEffects.update((PlayerEntity) (Object) this);
        ci.cancel();
    }

}
