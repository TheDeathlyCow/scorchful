package com.github.thedeathlycow.scorchful.gametest.common.cauldron;

import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("unused")
public class SandCauldronTests {

    @GameTest(
            structure = "scorchful-test:cauldron/sand_cauldron"
    )
    public void remove_sand_from_sand_cauldron(GameTestHelper context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.assertBlock(
                cauldronPos,
                block -> block == SBlocks.SAND_CAULDRON,
                block -> Component.literal("Sand Cauldron not present!")
        );

        final Player mockPlayer = context.makeMockPlayer(GameType.SURVIVAL);
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().hasAnyMatching(stack -> stack.is(Items.SAND));
        context.assertBlockPresent(Blocks.CAULDRON, cauldronPos);
        context.assertTrue(hasSand, Component.literal("Player should have sand!"));
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:cauldron/red_sand_cauldron"
    )
    public void remove_red_sand_from_red_sand_cauldron(GameTestHelper context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.assertBlock(
                cauldronPos,
                block -> block == SBlocks.RED_SAND_CAULDRON,
                block -> Component.literal("Red Sand Cauldron not present!")
        );

        final Player mockPlayer = context.makeMockPlayer(GameType.SURVIVAL);
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().hasAnyMatching(stack -> stack.is(Items.RED_SAND));
        context.assertBlockPresent(Blocks.CAULDRON, cauldronPos);
        context.assertTrue(hasSand, Component.literal("Player should have red sand!"));
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:cauldron/partially_filled_sand_cauldron"
    )
    public void try_remove_sand_from_partially_filled_sand_cauldron(GameTestHelper context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.assertBlock(
                cauldronPos,
                block -> block == SBlocks.SAND_CAULDRON,
                block -> Component.literal("Sand Cauldron not present!")
        );
        context.assertBlockProperty(
                cauldronPos,
                SandCauldronBlock.LEVEL,
                i -> i == SandCauldronBlock.MIN_LEVEL,
                Component.literal("Sand Cauldron is not partially filled!")
        );

        final Player mockPlayer = context.makeMockPlayer(GameType.SURVIVAL);
        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getInventory().hasAnyMatching(stack -> stack.is(Items.SAND));
        context.assertBlockPresent(SBlocks.SAND_CAULDRON, cauldronPos);
        context.assertBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MIN_LEVEL);
        context.assertFalse(hasSand, Component.literal("Player should NOT have sand!"));
        context.succeed();
    }

}
