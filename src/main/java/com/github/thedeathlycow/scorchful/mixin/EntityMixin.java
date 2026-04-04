package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.cca.ScorchfulCardinalEntityComponents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "shouldBeSaved",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelSaveIfDesertVisionChild(CallbackInfoReturnable<Boolean> cir) {
        Entity instance = (Entity) (Object) this;
        if (ScorchfulCardinalEntityComponents.ENTITY_DESERT_VISION.get(instance).hasDesertVision()) {
            cir.setReturnValue(false);
        }
    }
}
