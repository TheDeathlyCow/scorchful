package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
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
        if (instance.getData(ScorchfulEntityAttachments.ENTITY_DESERT_VISION).hasDesertVision()) {
            cir.setReturnValue(false);
        }
    }

}
