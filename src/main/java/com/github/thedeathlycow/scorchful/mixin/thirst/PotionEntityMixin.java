package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractThrownPotion.class)
public abstract class PotionEntityMixin extends ThrowableItemProjectile {
    protected PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "onHit",
            at = @At("HEAD")
    )
    private void onSplashPotionCollision(HitResult hitResult, CallbackInfo ci) {
        Level world = this.level();
        if (world.isClientSide()) {
            return;
        }
        AABB box = this.getBoundingBox().inflate(4.0, 2.0, 4.0);

        int waterAmount = ScorchfulConfig.getEntityConfig().getSoakingFromSplashPotions();

        world.getEntitiesOfClass(LivingEntity.class, box)
                .forEach(
                        entity -> {
                            int wetTicks = entity.thermoo$getWetTicks();
                            entity.thermoo$setWetTicks(wetTicks + waterAmount);
                        }
                );
    }
}
