package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.item.TurtleArmourTickHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void onTick(CallbackInfo ci) {
        TurtleArmourTickHandler.tick((PlayerEntity) (Object) this);
    }

}
