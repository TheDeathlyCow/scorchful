package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SMobEffects;
import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class FearEffect extends MobEffect {
    public static final int ARMOR_COLOR = ARGB.color(0x7a, 0x04, 0x21);

    public FearEffect(MobEffectCategory category, int color) {
        super(category, color, SParticleTypes.BAT);
    }

    public static boolean canHaveFear(LivingEntity entity, MobEffectInstance effectInstance) {
        if (effectInstance.is(SMobEffects.FEAR)) {
            if (entity.is(SEntityTypeTags.IMMUNE_TO_FEAR)) {
                return false;
            } else {
                return entity.isAlwaysTicking() || entity instanceof PathfinderMob;
            }
        }

        return true;
    }

    public static boolean isFeared(Entity target) {
        return target instanceof LivingEntity livingTarget
                && livingTarget.hasEffect(SMobEffects.FEAR);
    }

    public static double modifyDetectionDistance(LivingEntity target, double original) {
        return target.hasEffect(SMobEffects.FEAR)
                ? ScorchfulConfig.getEntityConfig().getFearDetectionRangeMultiplier() * original
                : original;
    }

}
