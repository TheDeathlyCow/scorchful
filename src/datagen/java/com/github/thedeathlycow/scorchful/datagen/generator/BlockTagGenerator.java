package com.github.thedeathlycow.scorchful.datagen.generator;

import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(SBlockTags.SAND_PILE_CANNOT_SURVIVE_ON)
                .add(Blocks.BARRIER);

        valueLookupBuilder(SBlockTags.SAND_PILE_CAN_SURVIVE_ON)
                .add(Blocks.HONEY_BLOCK)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.MUD)
                .addOptionalTag(BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER);

        valueLookupBuilder(SBlockTags.SAND_PILES)
                .add(SBlocks.SAND_PILE)
                .add(SBlocks.RED_SAND_PILE);

        valueLookupBuilder(SBlockTags.SAND_CAULDRONS)
                .add(SBlocks.SAND_CAULDRON)
                .add(SBlocks.RED_SAND_CAULDRON);

        valueLookupBuilder(SBlockTags.NETHER_ROOT_REPLACEABLE)
                .add(Blocks.NETHERRACK)
                .add(SBlocks.ROOTED_NETHERRACK)
                .add(SBlocks.ROOTED_WARPED_NYLIUM)
                .add(SBlocks.ROOTED_CRIMSON_NYLIUM)
                .addOptionalTag(BlockTags.NYLIUM)
                .addOptionalTag(ConventionalBlockTags.NETHERRACKS);

        valueLookupBuilder(SBlockTags.NETHER_LILY_CAN_ABSORB_WATER)
                .add(Blocks.ROOTED_DIRT)
                .add(SBlocks.ROOTED_NETHERRACK)
                .add(SBlocks.ROOTED_WARPED_NYLIUM)
                .add(SBlocks.ROOTED_CRIMSON_NYLIUM);

        valueLookupBuilder(SBlockTags.HEAVY_ICE)
                .addOptionalTag(BlockTags.ICE);

        valueLookupBuilder(ConventionalBlockTags.NETHERRACKS)
                .add(SBlocks.ROOTED_NETHERRACK);

        valueLookupBuilder(BlockTags.NYLIUM)
                .add(SBlocks.ROOTED_WARPED_NYLIUM)
                .add(SBlocks.ROOTED_CRIMSON_NYLIUM);
    }
}