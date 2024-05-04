package com.github.thedeathlycow.scorchful.testmod.common;

import net.minecraft.block.Blocks;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class CauldronInteractionTests {

    @GameTest(
            templateName = "scorchful-test:empty_cauldron"
    )
    public void add_sand_to_empty_cauldron(TestContext context) {

        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == Blocks.CAULDRON,
                () -> "Cauldron not present!"
        );

    }

}
