package com.github.thedeathlycow.scorchful.client.mixin;

import com.github.thedeathlycow.scorchful.client.SoakedEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
@Environment(EnvType.CLIENT)
public abstract class DrippingWetPlayerMixin extends LivingEntity {

    @Shadow
    protected boolean wasUnderwater;

    protected DrippingWetPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void dripParticles(CallbackInfo ci) {
        SoakedEffects.tickDripParticles((Player) (Object) this, this.level(), this.wasUnderwater);
    }

}
