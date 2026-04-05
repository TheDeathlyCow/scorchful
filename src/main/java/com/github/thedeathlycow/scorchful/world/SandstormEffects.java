package com.github.thedeathlycow.scorchful.world;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.WeatherConfig;
import com.github.thedeathlycow.scorchful.mixin.accessor.LivingEntityAccessor;
import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class SandstormEffects {
    private static final Identifier SPEED_MODIFIER_ID = Scorchful.id("sandstorm_slowing");
    private static final Identifier FOLLOW_RANGE_MODIFIER_ID = Scorchful.id("sandstorm_reduced_visibility");

    public static boolean canBreezesSpawnAt(
            ServerLevel level,
            BlockPos pos,
            Holder<Biome> biome
    ) {
        return ScorchfulConfig.getWeatherConfig().enableBreezesInSandstorms()
                && level.isRaining()
                && biome.is(SBiomeTags.SPAWNS_BREEZES_IN_STORMS)
                && level.canSeeSky(pos); // this prevents breezes in caves
    }

    public static boolean canSuffocate(LivingEntity entity) {
        WeatherConfig config = ScorchfulConfig.getWeatherConfig();

        if (config.enableSuffocatingSandstorms()) {
            if (!entity.is(SEntityTypeTags.SUFFOCATES_IN_SANDSTORMS)) {
                return false;
            } else if (entity instanceof Player player && player.getAbilities().invulnerable) {
                return false;
            } else if (config.requireThunderStormsForSuffocation() && !entity.level().isThundering()) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public static boolean tickSandstormEffects(LivingEntity entity, boolean wasInSandstorm) {
        if (entity.is(SEntityTypeTags.DOES_NOT_SLOW_IN_SANDSTORM)) {
            return false;
        }

        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        if (Sandstorms.getCurrentSandStorm(level, pos) == Sandstorms.SandstormType.NONE) {
            if (wasInSandstorm) {
                removeModifiers(entity);
            }
            return false;
        }

        if (!wasInSandstorm) {
            addSlow(entity);
        }

        if (canSuffocate(entity)) {
            LivingEntityAccessor accessor = (LivingEntityAccessor) entity;
            entity.setAirSupply(accessor.scorchfulInvokeDecreaseAirSupply(entity.getAirSupply()));

            if (accessor.scorchfulInvokeShouldTakeDrowningDamage()) {
                entity.setAirSupply(0);
                float damage = 2.0f * ScorchfulConfig.getWeatherConfig().suffocatingDamageMultiplier();
                entity.hurtServer(serverLevel, entity.damageSources().scorchful$Suffocate(), damage);
            }
        }

        return true;
    }

    private static void removeModifiers(LivingEntity entity) {
        removeModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID);
        removeModifier(entity, Attributes.FOLLOW_RANGE, FOLLOW_RANGE_MODIFIER_ID);
    }

    private static void addSlow(LivingEntity entity) {
        WeatherConfig config = ScorchfulConfig.getWeatherConfig();
        addModifier(
                entity,
                Attributes.MOVEMENT_SPEED,
                SPEED_MODIFIER_ID,
                config.getSandstormSlownessAmountPercent(),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        addModifier(
                entity,
                Attributes.FOLLOW_RANGE,
                FOLLOW_RANGE_MODIFIER_ID,
                config.getSandstormFollowRangeReductionPercent(),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    private static void addModifier(
            LivingEntity entity,
            Holder<Attribute> attribute,
            Identifier modifierID,
            double value,
            AttributeModifier.Operation operation
    ) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.addTransientModifier(
                    new AttributeModifier(
                            modifierID,
                            value,
                            operation
                    )
            );
        }
    }

    private static void removeModifier(
            LivingEntity entity,
            Holder<Attribute> attribute,
            Identifier modifierID
    ) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null && instance.getModifier(modifierID) != null) {
            instance.removeModifier(modifierID);
        }
    }

    private SandstormEffects() {

    }

}
