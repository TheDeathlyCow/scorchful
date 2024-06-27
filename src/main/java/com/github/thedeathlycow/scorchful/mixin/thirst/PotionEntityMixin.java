package com.github.thedeathlycow.scorchful.mixin.thirst;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {


    protected PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "onCollision",
            at = @At("HEAD")
    )
    private void onSplashPotionCollision(HitResult hitResult, CallbackInfo ci) {
        World world = this.getWorld();
        if (world.isClient) {
            return;
        }
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);

        int waterAmount = Scorchful.getConfig().thirstConfig.getSoakingFromSplashPotions();

        world.getNonSpectatingEntities(LivingEntity.class, box)
                .forEach(
                        entity -> {
                            int wetTicks = entity.thermoo$getWetTicks();
                            entity.thermoo$setWetTicks(wetTicks + waterAmount);
                        }
                );
    }

}
