package com.github.thedeathlycow.scorchful.datagen.generator.tag;

import com.github.thedeathlycow.scorchful.registry.tag.SEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends FabricTagsProvider.EntityTypeTagsProvider {
    public EntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(SEntityTypeTags.HAS_PLAYER_TEMPERATURE_STATUSES)
                .add(EntityType.PLAYER)
                .add(EntityType.MANNEQUIN);

        valueLookupBuilder(SEntityTypeTags.SUFFOCATES_IN_SANDSTORMS)
                .add(EntityType.PLAYER);
    }
}