package com.github.thedeathlycow.scorchful.gametest.common.cauldron;

import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("unused")
public class CauldronInteractionTests {

    @GameTest(
            structure = "scorchful-test:cauldron/empty_cauldron"
    )
    public void add_sand_to_empty_cauldron(GameTestHelper context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.assertBlock(
                cauldronPos,
                block -> block == Blocks.CAULDRON,
                block -> Component.literal("Cauldron not present!")
        );

        final Player mockPlayer = context.makeMockPlayer(GameType.SURVIVAL);
        mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, Items.SAND.getDefaultInstance());

        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.RED_SAND);
        context.assertBlockPresent(SBlocks.SAND_CAULDRON, cauldronPos);
        context.assertBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL);
        context.assertFalse(hasSand, Component.literal("Player should NOT be holding sand!"));
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:cauldron/empty_cauldron"
    )
    public void add_red_sand_to_empty_cauldron(GameTestHelper context) {
        final BlockPos cauldronPos = new BlockPos(2, 1, 2);
        context.assertBlock(
                cauldronPos,
                block -> block == Blocks.CAULDRON,
                block -> Component.literal("Cauldron not present!")
        );

        final Player mockPlayer = context.makeMockPlayer(GameType.SURVIVAL);
        mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, Items.RED_SAND.getDefaultInstance());

        context.useBlock(cauldronPos, mockPlayer);

        boolean hasSand = mockPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.RED_SAND);
        context.assertBlockPresent(SBlocks.RED_SAND_CAULDRON, cauldronPos);
        context.assertBlockProperty(cauldronPos, SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL);
        context.assertFalse(hasSand, Component.literal("Player should NOT be holding red sand!"));
        context.succeed();
    }

}
