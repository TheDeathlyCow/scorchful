package com.github.thedeathlycow.scorchful.datagen.generator.loot;

import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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

        add(SBlocks.RED_SAND_PILE, block -> createSandLayers(block, block, block, Blocks.RED_SAND));
        add(SBlocks.SAND_PILE, block -> createSandLayers(block, block, block, Blocks.SAND));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        super.generate(ScorchfulLootUtils.withSequenceId(output));
    }

    private LootTable.Builder createSandLayers(final Block block, final ItemLike drop, final ItemLike silkTouchDrop, final ItemLike fullDrop) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
                        .add(AlternativesEntry.alternatives(
                                this.fullBlockOrElse(block, drop, fullDrop).when(this.doesNotHaveSilkTouch()),
                                this.fullBlockOrElse(block, silkTouchDrop, fullDrop)
                        ))
                );
    }

    private LootPoolEntryContainer.Builder<?> fullBlockOrElse(final Block block, final ItemLike drop, final ItemLike fullDrop) {
        return AlternativesEntry.alternatives(
                SandPileBlock.LAYERS.getPossibleValues(),
                layers -> layers == SandPileBlock.MAX_LAYERS ? LootItem.lootTableItem(fullDrop) : LootItem.lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers)))
                        .when(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(
                                                StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(SandPileBlock.LAYERS, layers)
                                        )
                        )
        );
    }
}