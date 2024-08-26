package com.github.thedeathlycow.scorchful.mixin.client;

import com.github.thedeathlycow.scorchful.client.SoakedEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class DrippingWetPlayerMixin extends LivingEntity {

    @Shadow
    protected boolean isSubmergedInWater;

    protected DrippingWetPlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void dripParticles(CallbackInfo ci) {
        SoakedEffects.tickDripParticles((PlayerEntity) (Object) this, this.getWorld(), this.isSubmergedInWater);
    }

}
