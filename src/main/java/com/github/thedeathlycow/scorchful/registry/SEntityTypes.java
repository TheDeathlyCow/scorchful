package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.HeatVisionEntity;
import com.github.thedeathlycow.scorchful.registry.tag.SDamageTypeTags;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class SEntityTypes {
    public static final EntityType<HeatVisionEntity> HEAT_VISION = register(
            "heat_vision",
            EntityType.Builder.create(
                    (type, world) -> new HeatVisionEntity(world, HeatVisionDefinition.EMPTY),
                    SpawnGroup.MISC
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful entity types");

        ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (entity, source, baseDamageTaken, damageTaken, blocked) -> {
                    if (!blocked && source.isIn(SDamageTypeTags.FIREBALL)) {
                        entity.thermoo$addTemperature(
                                Scorchful.getConfig().heatingConfig.getFireballHeat(),
                                HeatingModes.ACTIVE
                        );
                    }
                }
        );
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Scorchful.id(id));
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }

    private SEntityTypes() {

    }
}
