package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;

public class NaturesSpiritPatch implements DependentModInitializer {
    @Override
    public void onInitialize() {
        Block pinkSandPileBlock = SBlocks.register(
                "natures_spirit/pink_sand_pile",
                settings -> new SandPileBlock(
                        0xDAAF88,
                        settings
                                .replaceable()
                                .notSolid()
                                .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                                .pistonBehavior(PistonBehavior.DESTROY)
                ),
                AbstractBlock.Settings.copy(Blocks.SAND)
        );

        SItems.register(
                "natures_spirit/pink_sand_pile",
                settings -> new BlockItem(pinkSandPileBlock, settings)
        );

        SandAccumulation.SANDSTORM_BLOCK_TYPES.get().put(Sandstorms.SandstormType.PINK, pinkSandPileBlock);
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[] { ScorchfulIntegrations.NATURES_SPIRIT_ID };
    }
}