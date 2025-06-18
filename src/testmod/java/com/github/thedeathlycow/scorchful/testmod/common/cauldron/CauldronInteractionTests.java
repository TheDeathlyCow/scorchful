package com.github.thedeathlycow.scorchful.testmod.common.cauldron;

import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

@SuppressWarnings("unused")
public class CauldronInteractionTests {

    @GameTest(
            structure = "scorchful-test:cauldron/empty_cauldron"
    )
    public void add_sand_to_empty_cauldron(TestContext context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == Blocks.CAULDRON,
                block -> Text.literal("Cauldron not present!")
        );

        final PlayerEntity mockPlayer = context.createMockPlayer(GameMode.SURVIVAL);
        mockPlayer.setStackInHand(Hand.MAIN_HAND, Items.SAND.getDefaultStack());

        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getStackInHand(Hand.MAIN_HAND).isOf(Items.RED_SAND);
        context.expectBlock(SBlocks.SAND_CAULDRON, cauldronPos);
        context.expectBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL);
        context.assertFalse(hasSand, Text.literal("Player should NOT be holding sand!"));
        context.complete();
    }

    @GameTest(
            structure = "scorchful-test:cauldron/empty_cauldron"
    )
    public void add_red_sand_to_empty_cauldron(TestContext context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.checkBlock(
                cauldronPos,
                block -> block == Blocks.CAULDRON,
                block -> Text.literal("Cauldron not present!")
        );

        final PlayerEntity mockPlayer = context.createMockPlayer(GameMode.SURVIVAL);
        mockPlayer.setStackInHand(Hand.MAIN_HAND, Items.RED_SAND.getDefaultStack());

        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getStackInHand(Hand.MAIN_HAND).isOf(Items.RED_SAND);
        context.expectBlock(SBlocks.RED_SAND_CAULDRON, cauldronPos);
        context.expectBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL);
        context.assertFalse(hasSand, Text.literal("Player should NOT be holding red sand!"));
        context.complete();
    }

}
