package com.github.thedeathlycow.scorchful.entity.ai;

import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class FearTargetGoal extends Goal {

    private static final double FAST_FLEE_RANGE = 7.0;

    private final PathfinderMob mob;
    private final double slowSpeed;
    private final double fastSpeed;
    @Nullable
    private Path fleePath;

    public FearTargetGoal(PathfinderMob mob) {
        this(mob, 1.0, 1.2);
    }

    public FearTargetGoal(PathfinderMob mob, double slowSpeed, double fastSpeed) {
        this.mob = mob;
        this.slowSpeed = slowSpeed;
        this.fastSpeed = fastSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() == null || !this.mob.hasEffect(SStatusEffects.FEAR)) {
            return false;
        }

        this.fleePath = this.findFleePath(this.mob.getTarget());
        return this.fleePath != null;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.fleePath, 1.0);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    private Path findFleePath(LivingEntity target) {
        Vec3 targetPos = DefaultRandomPos.getPosAway(this.mob, 16, 7, target.position());
        return targetPos != null
                ? this.mob.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0)
                : null;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && this.mob.distanceToSqr(target) < FAST_FLEE_RANGE * FAST_FLEE_RANGE) {
            this.mob.getNavigation().setSpeedModifier(this.fastSpeed);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.slowSpeed);
        }
    }
}
