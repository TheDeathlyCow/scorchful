package com.github.thedeathlycow.scorchful.entity.effect;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.github.thedeathlycow.scorchful.registry.SStatusEffects;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class FearStatusEffect extends StatusEffect {
    public FearStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, SParticleTypes.BAT);
    }

    public static boolean canHaveFear(LivingEntity entity) {
        if (entity.getType().isIn(SEntityTypeTags.IMMUNE_TO_FEAR)) {
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

    public static void onMesmerizedActivated(LivingEntity mesmerized, LivingEntity mesmerizing) {
        World world = mesmerized.getWorld();
        if (!world.isClient()) {
            mesmerized.addStatusEffect(new StatusEffectInstance(SStatusEffects.FEAR, 60), mesmerizing);
            DamageSource source = scare(world, mesmerizing);
            mesmerized.damage(source, 10);
        }
    }

    public static DamageSource scare(World world, LivingEntity attacker) {
        Registry<DamageType> registry = world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE);
        return new DamageSource(registry.entryOf(SDamageTypes.SCARE), attacker);
    }

}
