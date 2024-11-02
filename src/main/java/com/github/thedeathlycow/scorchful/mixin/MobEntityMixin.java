package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.entity.ai.FearTargetGoal;
import com.github.thedeathlycow.scorchful.entity.ai.MesmerizedTargetGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Shadow
    @Final
    protected GoalSelector goalSelector;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initFearTargetGoal(CallbackInfo ci) {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            MobEntity instance = (MobEntity) (Object) this;
            if (instance instanceof PathAwareEntity pathAware) {
                this.goalSelector.add(1, new FearTargetGoal(pathAware));
                this.goalSelector.add(1, new MesmerizedTargetGoal(pathAware));
            }
        }
    }
}
