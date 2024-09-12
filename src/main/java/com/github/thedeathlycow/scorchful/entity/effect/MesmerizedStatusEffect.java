package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;

import java.util.function.Predicate;

public class MesmerizedStatusEffect extends StatusEffect {

    public static final Predicate<Entity> IS_VALID_TARGET = EntityPredicates.EXCEPT_SPECTATOR
            .and(Entity::isPlayer);

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
}
