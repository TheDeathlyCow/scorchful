package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.WeatherConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class SandstormSlowing {

    private static final Identifier ATTRIBUTE_ID = Scorchful.id("sandstorm_slowing");

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
                removeSlow(entity);
            }
            return false;
        }

        if (!wasInSandstorm) {
            addSlow(entity);
        }

        return true;
    }

    private static void removeSlow(LivingEntity entity) {
        EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (instance != null && instance.getModifier(ATTRIBUTE_ID) != null) {
            instance.removeModifier(ATTRIBUTE_ID);
        }
    }

    private static void addSlow(LivingEntity entity) {
        EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (instance != null) {

            WeatherConfig config = Scorchful.getConfig().weatherConfig;
            double value = config.getSandstormSlownessAmountPercent();

            instance.addTemporaryModifier(
                    new EntityAttributeModifier(
                            ATTRIBUTE_ID,
                            value,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
        }
    }

    private SandstormSlowing() {

    }

}
