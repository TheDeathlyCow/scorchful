package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.attachment.EntityDesertVisionAttachment;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

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

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void tickVision(CallbackInfo ci) {
        if (this.level().isClientSide) {
            return;
        }
        Entity instance = (Entity) (Object) this;
        EntityDesertVisionAttachment attachment = instance.getData(ScorchfulEntityAttachments.ENTITY_DESERT_VISION);
        if (attachment.hasDesertVision()) {
            attachment.serverTick();
        }
    }
}
