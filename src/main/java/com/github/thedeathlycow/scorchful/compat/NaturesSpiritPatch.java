package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.piston.PistonBehavior;

public class NaturesSpiritPatch implements DependentModInitializer {

    @Override
    public void onInitialize() {
        Block pinkSandPile = SBlocks.register(
                "pink_sand_pile",
                settings -> new SandPileBlock(
                        0xDBD3A0,
                        settings
                                .replaceable()
                                .notSolid()
                                .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                                .pistonBehavior(PistonBehavior.DESTROY)
                ),
                AbstractBlock.Settings.copy(Blocks.SAND)
        );
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[] { ScorchfulIntegrations.NATURES_SPIRIT_ID };
    }
}