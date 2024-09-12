package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.components.MesmerizedComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class MesmerizedStatusEffect extends StatusEffect {

    public static final Predicate<Entity> IS_VALID_TARGET = EntityPredicates.EXCEPT_SPECTATOR
            .and(Entity::isPlayer);

    public static final Event<OnApproachedMesmerTarget> ON_APPROACHED_MESMER_TARGET_EVENT = EventFactory.createArrayBacked(
            OnApproachedMesmerTarget.class,
            listeners -> (mesmerized, mesmerizing) -> {
                for (OnApproachedMesmerTarget listener : listeners) {
                    listener.onApproach(mesmerized, mesmerizing);
                }
            }
    );

    public MesmerizedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public static boolean canBeMesmerized(LivingEntity entity) {
        if (entity.getType().isIn(SEntityTypeTags.IMMUNE_TO_MESMERIZED) || entity.hasStatusEffect(SStatusEffects.FEAR)) {
            return false;
        } else {
            return entity.isPlayer() || entity instanceof PathAwareEntity;
        }
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        MesmerizedComponent component = ScorchfulComponents.MESMERIZED.get(entity);
        if (component.hasMesmerizedTarget()) {
            Vec3d targetPos = component.getMesmerizedTarget().getPos();
            Vec3d entityPos = entity.getPos();
            if (entityPos.isInRange(targetPos, 2)) {
                ON_APPROACHED_MESMER_TARGET_EVENT.invoker().onApproach(entity, component.getMesmerizedTarget());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @FunctionalInterface
    public interface OnApproachedMesmerTarget {
        void onApproach(LivingEntity mesmerized, Entity mesmerizing);
    }
}
