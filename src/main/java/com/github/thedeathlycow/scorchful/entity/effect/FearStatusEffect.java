package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FearStatusEffect extends StatusEffect {
    public FearStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public static boolean isFeared(Entity target) {
        return target instanceof LivingEntity livingTarget
                && livingTarget.hasStatusEffect(SStatusEffects.FEAR);
    }

    public static double modifyDetectionDistance(LivingEntity target, double original) {
        return target.hasStatusEffect(SStatusEffects.FEAR)
                ? 2.0 * original
                : original;
    }

}
