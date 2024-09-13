package com.github.thedeathlycow.scorchful.entity.ai;

import com.github.thedeathlycow.scorchful.components.MesmerizedComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.entity.effect.MesmerizedStatusEffect;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class MesmerizedTargetGoal extends Goal {

    private final PathAwareEntity mob;
    private final double speed;
    @Nullable
    private Path mesmerizedPath;

    public MesmerizedTargetGoal(PathAwareEntity mob) {
        this(mob, 0.75);
    }

    public MesmerizedTargetGoal(PathAwareEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.mob.hasStatusEffect(SStatusEffects.MESMERIZED);
    }

    @Override
    public void tick() {
        MesmerizedComponent component = ScorchfulComponents.MESMERIZED.get(this.mob);
        this.updatePathIfNeeded(component);
    }

    @Override
    public boolean shouldContinue() {
        return this.mob.hasStatusEffect(SStatusEffects.MESMERIZED);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mesmerizedPath = null;
    }

    private void updatePath() {
        MesmerizedComponent component = ScorchfulComponents.MESMERIZED.get(this.mob);
        this.mob.getNavigation().startMovingTo(component.getMesmerizedTarget(), this.speed);
    }

    private void updatePathIfNeeded(MesmerizedComponent component) {
        if (component.hasMesmerizedTarget()) {
            this.mob.getLookControl()
                    .lookAt(component.getMesmerizedTarget(), 10.0f, this.mob.getMaxLookPitchChange());

            BlockPos mesmerTargetPos = component.getMesmerizedTarget().getBlockPos();
            if (this.mesmerizedPath == null || !mesmerTargetPos.isWithinDistance(this.mesmerizedPath.getTarget(), 2)) {
                this.updatePath();
            }
        }
    }
}
