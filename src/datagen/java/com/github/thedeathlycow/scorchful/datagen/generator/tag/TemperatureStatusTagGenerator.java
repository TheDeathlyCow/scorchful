package com.github.thedeathlycow.scorchful.datagen.generator.tag;

import com.github.thedeathlycow.scorchful.registry.STemperatureStatuses;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.TemperatureStatusTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class TemperatureStatusTagGenerator extends FabricTagsProvider<TemperatureStatus> {
    public TemperatureStatusTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, ThermooRegistries.TEMPERATURE_STATUS, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(TemperatureStatusTags.APPLICATION_ORDER)
                .add(STemperatureStatuses.PLAYER_HEART_BEAT_SLOW)
                .add(STemperatureStatuses.PLAYER_HEART_BEAT_MEDIUM)
                .add(STemperatureStatuses.PLAYER_HEART_BEAT_FAST)
                .add(STemperatureStatuses.PLAYER_HEART_BEAT_RACING)

                .add(STemperatureStatuses.PLAYER_WARM)
                .add(STemperatureStatuses.PLAYER_HOT)
                .add(STemperatureStatuses.PLAYER_OVERHEATING)
                .add(STemperatureStatuses.PLAYER_HEAT_STROKE)

                .add(STemperatureStatuses.DOG_PANTING)
                .add(STemperatureStatuses.HEAT_DAMAGE);
    }
}