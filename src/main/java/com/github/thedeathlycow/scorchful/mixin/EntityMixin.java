package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "shouldSave",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelSaveIfDesertVisionChild(CallbackInfoReturnable<Boolean> cir) {
        Entity instance = (Entity) (Object) this;
        if (ScorchfulComponents.ENTITY.get(instance).isDesertVisionChild()) {
            cir.setReturnValue(false);
        }
    }

}
