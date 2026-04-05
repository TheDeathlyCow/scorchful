package com.github.thedeathlycow.scorchful.gametest.common.netherlily;

import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class WarpedLilyTests {

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_wet",
            environment = "scorchful-test:no_random_ticks"
    )
    public void using_glass_bottle_on_wet_warped_lily_fills_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Component.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Newly created player should have a glass bottle")
        );


        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), Component.literal("Player should have a water bottle"));
        context.assertFalse(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Player should NOT have a glass bottle")
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_dry",
            environment = "scorchful-test:no_random_ticks"
    )
    public void using_glass_bottle_on_dry_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Component.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Newly created player should have a glass bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), Component.literal("Player should NOT have a water bottle"));
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Player should have a glass bottle")
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_partially_wet",
            environment = "scorchful-test:no_random_ticks"
    )
    public void using_glass_bottle_on_partially_wet_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Component.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Newly created player should have a glass bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), Component.literal("Player should NOT have a water bottle"));
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Component.literal("Player should have a glass bottle")
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_wet",
            environment = "scorchful-test:no_random_ticks"
    )
    public void using_water_skin_on_wet_warped_lily_fills_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, SItems.WATER_SKIN.getDefaultInstance());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                Component.literal("Newly created Water Skin should be empty")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(isWaterSkinEmpty.getAsBoolean(), Component.literal("Water Skin should NOT be empty!"));
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_dry",
            environment = "scorchful-test:no_random_ticks"
    )
    public void using_water_skin_on_dry_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, SItems.WATER_SKIN.getDefaultInstance());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                Component.literal("Newly created Water Skin should be empty")
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), Component.literal("Water Skin should be empty!"));
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

}
