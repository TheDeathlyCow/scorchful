package com.github.thedeathlycow.scorchful.testmod.common.cauldron;

import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class SandCauldronTests {

    @GameTest(
            templateName = "scorchful-test:cauldron/sand_cauldron"
    )
    public void remove_sand_from_sand_cauldron(TestContext context) {
        final BlockPos cauldronPos = new BlockPos(2, 2, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == SBlocks.SAND_CAULDRON,
                () -> "Sand Cauldron not present!"
        );

        final PlayerEntity mockPlayer = context.createMockSurvivalPlayer();
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().containsAny(stack -> stack.isOf(Items.SAND));
        context.expectBlock(Blocks.CAULDRON, cauldronPos);
        context.assertTrue(hasSand, "Player should have sand!");
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:cauldron/red_sand_cauldron"
    )
    public void remove_red_sand_from_red_sand_cauldron(TestContext context) {
        final BlockPos cauldronPos = new BlockPos(2, 2, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == SBlocks.RED_SAND_CAULDRON,
                () -> "Red Sand Cauldron not present!"
        );

        final PlayerEntity mockPlayer = context.createMockSurvivalPlayer();
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().containsAny(stack -> stack.isOf(Items.RED_SAND));
        context.expectBlock(Blocks.CAULDRON, cauldronPos);
        context.assertTrue(hasSand, "Player should have red sand!");
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:cauldron/partially_filled_sand_cauldron"
    )
    public void try_remove_sand_from_partially_filled_sand_cauldron(TestContext context) {
        final BlockPos cauldronPos = new BlockPos(2, 2, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == SBlocks.SAND_CAULDRON,
                () -> "Sand Cauldron not present!"
        );
        context.checkBlockProperty(
                cauldronPos,
                SandCauldronBlock.LEVEL,
                i -> i == SandCauldronBlock.MIN_LEVEL,
                "Sand Cauldron is not partially filled!"
        );

        final PlayerEntity mockPlayer = context.createMockSurvivalPlayer();
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().containsAny(stack -> stack.isOf(Items.SAND));
        context.expectBlock(SBlocks.SAND_CAULDRON, cauldronPos);
        context.expectBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MIN_LEVEL);
        context.assertFalse(hasSand, "Player should NOT have sand!");
        context.complete();
    }

}
