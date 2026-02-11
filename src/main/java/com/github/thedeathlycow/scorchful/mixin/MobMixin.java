package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.ai.FearTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Shadow
    @Final
    protected GoalSelector goalSelector;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initFearTargetGoal(CallbackInfo ci) {
        if (this.level() != null && !this.level().isClientSide()) {
            Mob instance = (Mob) (Object) this;
            if (instance instanceof PathfinderMob pathAware) {
                this.goalSelector.addGoal(1, new FearTargetGoal(pathAware));
            }
        }
    }
}
