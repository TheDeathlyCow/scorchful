package com.github.thedeathlycow.scorchful.entity.ai;

import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FearTargetGoal extends Goal {

    private static final double FAST_FLEE_RANGE = 7.0;

    private final PathAwareEntity mob;
    private final double slowSpeed;
    private final double fastSpeed;
    @Nullable
    private Path fleePath;

    public FearTargetGoal(PathAwareEntity mob) {
        this(mob, 1.0, 1.2);
    }

    public FearTargetGoal(PathAwareEntity mob, double slowSpeed, double fastSpeed) {
        this.mob = mob;
        this.slowSpeed = slowSpeed;
        this.fastSpeed = fastSpeed;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.mob.getTarget() == null || !this.mob.hasStatusEffect(SStatusEffects.FEAR)) {
            return false;
        }

        this.fleePath = this.findFleePath(this.mob.getTarget());
        return this.fleePath != null;
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingAlong(this.fleePath, 1.0);
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    private Path findFleePath(LivingEntity target) {
        Vec3d targetPos = NoPenaltyTargeting.findFrom(this.mob, 16, 7, target.getPos());
        return targetPos != null
                ? this.mob.getNavigation().findPathTo(targetPos.x, targetPos.y, targetPos.z, 0)
                : null;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && this.mob.squaredDistanceTo(target) < FAST_FLEE_RANGE * FAST_FLEE_RANGE) {
            this.mob.getNavigation().setSpeed(this.fastSpeed);
        } else {
            this.mob.getNavigation().setSpeed(this.slowSpeed);
        }
    }
}
