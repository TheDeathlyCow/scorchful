package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SandstormSlowing {

    private static final Identifier SPEED_MODIFIER_ID = Scorchful.id("sandstorm_slowing");
    private static final Identifier FOLLOW_RANGE_MODIFIER_ID = Scorchful.id("sandstorm_reduced_visibility");

    public static boolean tickSandstormSlow(LivingEntity entity, boolean wasInSandstorm) {

        if (entity.getType().isIn(SEntityTypeTags.DOES_NOT_SLOW_IN_SANDSTORM)) {
            return false;
        }

        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();

        if (world.isClient) {
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
        removeModifier(entity, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER_ID);
        removeModifier(entity, EntityAttributes.GENERIC_FOLLOW_RANGE, FOLLOW_RANGE_MODIFIER_ID);
    }

    private static void addSlow(LivingEntity entity) {
        WeatherConfig config = Scorchful.getConfig().weatherConfig;
        addModifier(
                entity,
                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                SPEED_MODIFIER_ID,
                config.getSandstormSlownessAmountPercent(),
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        addModifier(
                entity,
                EntityAttributes.GENERIC_FOLLOW_RANGE,
                FOLLOW_RANGE_MODIFIER_ID,
                config.getSandstormFollowRangeReductionPercent(),
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    private static void addModifier(
            LivingEntity entity,
            RegistryEntry<EntityAttribute> attribute,
            Identifier modifierID,
            double value,
            EntityAttributeModifier.Operation operation
    ) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.addTemporaryModifier(
                    new EntityAttributeModifier(
                            modifierID,
                            value,
                            operation
                    )
            );
        }
    }

    private static void removeModifier(
            LivingEntity entity,
            RegistryEntry<EntityAttribute> attribute,
            Identifier modifierID
    ) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null && instance.getModifier(modifierID) != null) {
            instance.removeModifier(modifierID);
        }
    }

    private SandstormSlowing() {

    }

}
