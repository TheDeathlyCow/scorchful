package com.github.thedeathlycow.scorchful.datagen.generator.registry;

import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import com.github.thedeathlycow.scorchful.registry.STemperatureStatuses;
import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import com.github.thedeathlycow.scorchful.temperature.ChangeTemperatureEffect;
import com.github.thedeathlycow.scorchful.temperature.WolfPantSoundEffect;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect.DamageEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class TemperatureStatusGenerator extends FabricDynamicRegistryProvider {
    public TemperatureStatusGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        HolderLookup<EntityType<?>> entityTypes = provider.lookupOrThrow(Registries.ENTITY_TYPE);
        HolderLookup<TemperatureSource> temperatureSources = provider.lookupOrThrow(ThermooRegistries.TEMPERATURE_SOURCE);

        entries.add(
                STemperatureStatuses.HEAT_DAMAGE,
                TemperatureStatus.builder(TemperatureStatus.selectAllEntities().temperatureIsAtLeast(0.99))
                        .withInterval(20)
                        .addEffect(DamageEffect.create(1.0f, SDamageTypes.HEAT))
                        .build()
        );


        entries.add(
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
    }

    @Override
    public String getName() {
        return "ScorchfulTemperatureStatusGenerator";
    }
}