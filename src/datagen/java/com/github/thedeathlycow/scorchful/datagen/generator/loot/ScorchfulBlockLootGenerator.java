package com.github.thedeathlycow.scorchful.datagen.generator.loot;

import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ScorchfulBlockLootGenerator extends FabricBlockLootSubProvider {
    public ScorchfulBlockLootGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(SBlocks.CRIMSON_LILY);
        dropSelf(SBlocks.WARPED_LILY);

        dropOther(SBlocks.RED_SAND_CAULDRON, Blocks.CAULDRON);
        dropOther(SBlocks.SAND_CAULDRON, Blocks.CAULDRON);

        add(SBlocks.ROOTED_NETHERRACK, block -> this.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
        add(SBlocks.ROOTED_CRIMSON_NYLIUM, block -> this.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
        add(SBlocks.ROOTED_WARPED_NYLIUM, block -> this.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        super.generate(ScorchfulLootUtils.withSequenceId(output));
    }
}