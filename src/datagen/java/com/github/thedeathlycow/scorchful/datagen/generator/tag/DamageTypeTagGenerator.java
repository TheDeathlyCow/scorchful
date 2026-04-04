package com.github.thedeathlycow.scorchful.datagen.generator.tag;

import com.github.thedeathlycow.scorchful.registry.SDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends FabricTagsProvider<DamageType> {
    public DamageTypeTagGenerator(FabricPackOutput output,  CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.DAMAGE_TYPE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(DamageTypeTags.BYPASSES_ARMOR)
                .add(SDamageTypes.HEAT)
                .add(SDamageTypes.SUFFOCATE);

        builder(DamageTypeTags.BYPASSES_WOLF_ARMOR)
                .add(SDamageTypes.HEAT)
                .add(SDamageTypes.SUFFOCATE);

        builder(DamageTypeTags.IS_FIRE)
                .add(SDamageTypes.HEAT);

        builder(DamageTypeTags.NO_KNOCKBACK)
                .add(SDamageTypes.HEAT)
                .add(SDamageTypes.SUFFOCATE);

        builder(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)
                .add(SDamageTypes.HEAT)
                .add(SDamageTypes.SUFFOCATE);
    }
}