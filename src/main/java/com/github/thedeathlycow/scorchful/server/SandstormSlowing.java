package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class SandstormSlowing {

    private static final Identifier SPEED_MODIFIER_ID = Scorchful.id("sandstorm_slowing");
    private static final Identifier FOLLOW_RANGE_MODIFIER_ID = Scorchful.id("sandstorm_reduced_visibility");

    public static boolean tickSandstormSlow(LivingEntity entity, boolean wasInSandstorm) {

        if (entity.getType().is(SEntityTypeTags.DOES_NOT_SLOW_IN_SANDSTORM)) {
            return false;
        }

        Level world = entity.level();
        BlockPos pos = entity.blockPosition();

        if (world.isClientSide()) {
            return false;
        }

        if (Sandstorms.getCurrentSandStorm(world, pos) == Sandstorms.SandstormType.NONE) {
            if (wasInSandstorm) {
                removeModifiers(entity);
            }
            return false;
        }

        if (!wasInSandstorm) {
            addSlow(entity);
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

    private SandstormSlowing() {

    }

}
