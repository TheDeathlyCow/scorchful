package com.github.thedeathlycow.scorchful.datagen.generator.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import com.github.thedeathlycow.scorchful.registry.SMobEffects;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.registry.STemperatureStatuses;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import com.github.thedeathlycow.scorchful.temperature.ChangeTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.SoundTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.WolfPantSoundEffect;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.AttributeModifierEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.DamageEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.MobEffectEffect;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public final class TemperatureStatusGenerator {

    public static void bootstrap(BootstrapContext<TemperatureStatus> context) {
        HolderGetter<TemperatureSource> temperatureSources = context.lookup(ThermooRegistries.TEMPERATURE_SOURCE);

        HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);
        HolderSet<EntityType<?>> playerAffected = entityTypes.getOrThrow(SEntityTypeTags.HAS_PLAYER_TEMPERATURE_STATUSES);
        HolderSet<EntityType<?>> striderAffected = HolderSet.direct(EntityType::builtInRegistryHolder, EntityType.STRIDER);

        context.register(
                STemperatureStatuses.HEAT_DAMAGE,
                TemperatureStatus.builder(TemperatureStatus.selectAllEntities().temperatureIsAtLeast(0.99))
                        .withInterval(20)
                        .addEffect(DamageEffect.create(1.0f, SDamageTypes.HEAT))
                        .build()
        );

        context.register(
                STemperatureStatuses.DOG_PANTING,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(entityTypes.getOrThrow(SEntityTypeTags.MOBS_THAT_PANT))
                                        .temperatureIsAtLeast(0.25)
                        )
                        .withInterval(20)
                        .addEffect(new WolfPantSoundEffect(0.75f))
                        .addEffect(new ChangeTemperatureEffect(-20, temperatureSources.getOrThrow(TemperatureSources.PASSIVE)))
                        .build()
        );

        context.register(
                STemperatureStatuses.PLAYER_WARM,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(playerAffected)
                                        .temperatureIsAtLeast(0.5f)
                        )
                        .withInterval(40)
                        .addEffect(
                                MobEffectEffect.builder()
                                        .addEffect(MobEffectEffect.effect(MobEffects.WEAKNESS))
                                        .build()
                        )
                        .build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HOT,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(playerAffected)
                                        .temperatureIsAtLeast(0.75f)
                        )
                        .withInterval(40)
                        .addEffect(
                                MobEffectEffect.builder()
                                        .addEffect(MobEffectEffect.effect(MobEffects.MINING_FATIGUE))
                                        .addEffect(MobEffectEffect.effect(MobEffects.HUNGER))
                                        .build()
                        )
                        .build()
        );

        context.register(
                STemperatureStatuses.PLAYER_OVERHEATING,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(playerAffected)
                                        .temperatureIsAtLeast(0.99f)
                        )
                        .withInterval(40)
                        .addEffect(
                                MobEffectEffect.builder()
                                        .addEffect(MobEffectEffect.effect(MobEffects.WEAKNESS).withAmplifier(1))
                                        .addEffect(MobEffectEffect.effect(MobEffects.HUNGER).withAmplifier(1))
                                        .build()
                        )
                        .build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HEAT_STROKE,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(playerAffected)
                                        .withCondition(doesNotHaveFireResistance())
                                        .temperatureIsAtLeast(0.99f)
                        )
                        .withInterval(40)
                        .addEffect(
                                MobEffectEffect.builder()
                                        .addEffect(MobEffectEffect.effect(SMobEffects.HEAT_STROKE).withDuration(40))
                                        .build()
                        )
                        .build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HEART_BEAT_SLOW,
                heartBeatEffect(playerAffected, 66, 0.5, 0.75).build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HEART_BEAT_MEDIUM,
                heartBeatEffect(playerAffected, 22, 0.75, 0.9).build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HEART_BEAT_FAST,
                heartBeatEffect(playerAffected, 11, 0.9, 0.99).build()
        );

        context.register(
                STemperatureStatuses.PLAYER_HEART_BEAT_RACING,
                heartBeatEffect(playerAffected, 6, 0.99, 1.01).build()
        );

        context.register(
                STemperatureStatuses.STRIDER_HOT,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(striderAffected)
                                        .temperatureIsAtLeast(0.99f)
                        )
                        .withInterval(40)
                        .addEffect(
                                MobEffectEffect.builder()
                                        .addEffect(MobEffectEffect.effect(MobEffects.REGENERATION).withDuration(40))
                                        .build()
                        )
                        .addEffect(
                                AttributeModifierEffect.create(
                                        Attributes.ARMOR,
                                        6,
                                        Scorchful.id("temperature_effect.strider_armor"),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
                        .build()
        );

        context.register(
                STemperatureStatuses.STRIDER_SPEED,
                TemperatureStatus.builder(
                                TemperatureStatus.selector(striderAffected)
                                        .temperatureIsAtLeast(0f)
                        )
                        .withInterval(1)
                        .addEffect(
                                AttributeModifierEffect.createScaled(
                                        Attributes.MOVEMENT_SPEED,
                                        0.1,
                                        Scorchful.id("temperature_effect.strider_speed"),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
                        .build()
        );
    }

    private static TemperatureStatus.Builder heartBeatEffect(
            HolderSet<EntityType<?>> playerAffected,
            int interval,
            double scaleMin,
            double scaleMax
    ) {
        return TemperatureStatus.builder(
                        TemperatureStatus.selector(playerAffected)
                                .temperatureIsBetween(scaleMin, scaleMax)
                )
                .withInterval(interval)
                .addEffect(
                        new SoundTemperatureEffect(
                                SSoundEvents.TEMPERATURE_EFFECT_HEARTBEAT,
                                SoundSource.PLAYERS,
                                true,
                                new ConstantFloat(1f),
                                new ConstantFloat(1f)
                        )
                );
    }

    private static LootItemCondition.Builder doesNotHaveFireResistance() {
        return InvertedLootItemCondition.invert(
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                                .effects(
                                        MobEffectsPredicate.Builder.effects()
                                                .and(MobEffects.FIRE_RESISTANCE)
                                )
                )
        );
    }
}