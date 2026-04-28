package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.tag.SDamageTypeTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public final class SEntityTypes {
    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful entity types");

        ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (entity, source, baseDamageTaken, damageTaken, blocked) -> {
                    if (!blocked && source.is(SDamageTypeTags.FIREBALL)) {
                        entity.thermoo$addTemperature(
                                Scorchful.getConfig().heatingConfig.getFireballHeat(),
                                HeatingModes.ACTIVE
                        );
                    }
                }
        );
    }

    private static void register(String id, EntityType<?> type) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Scorchful.id(id), type);
    }

    private SEntityTypes() {

    }
}
