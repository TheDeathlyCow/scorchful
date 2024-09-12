package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;

public class FearStatusEffect extends StatusEffect {
    public FearStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, SParticleTypes.BAT);
    }

    public static boolean canHaveFear(LivingEntity entity) {
        if (entity.getType().isIn(SEntityTypeTags.IMMUNE_TO_FEAR) || entity.hasStatusEffect(SStatusEffects.MESMERIZED)) {
            return false;
        } else {
            return entity.isPlayer() || entity instanceof PathAwareEntity;
        }
    }

    public static boolean isFeared(Entity target) {
        return target instanceof LivingEntity livingTarget
                && livingTarget.hasStatusEffect(SStatusEffects.FEAR);
    }

    public static double modifyDetectionDistance(LivingEntity target, double original) {
        return target.hasStatusEffect(SStatusEffects.FEAR)
                ? Scorchful.getConfig().combatConfig.getFearDetectionRangeMultiplier() * original
                : original;
    }

}
