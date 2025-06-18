package com.github.thedeathlycow.scorchful.testmod.common.netherlily;

import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class WarpedLilyTests {

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_wet"
    )
    public void using_glass_bottle_on_wet_warped_lily_fills_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultStack());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Text.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Newly created player should have a glass bottle")
        );


        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), Text.literal("Player should have a water bottle"));
        context.assertFalse(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Player should NOT have a glass bottle")
        );
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.complete();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_dry"
    )
    public void using_glass_bottle_on_dry_warped_lily_does_not_fill_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultStack());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Text.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Newly created player should have a glass bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), Text.literal("Player should NOT have a water bottle"));
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Player should have a glass bottle")
        );
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.complete();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_partially_wet"
    )
    public void using_glass_bottle_on_partially_wet_warped_lily_does_not_fill_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultStack());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                Text.literal("Newly created player should not have any water bottles")
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Newly created player should have a glass bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), Text.literal("Player should NOT have a water bottle"));
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                Text.literal("Player should have a glass bottle")
        );
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.complete();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_wet"
    )
    public void using_water_skin_on_wet_warped_lily_fills_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, SItems.WATER_SKIN.getDefaultStack());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getStackInHand(Hand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                Text.literal("Newly created Water Skin should be empty")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(isWaterSkinEmpty.getAsBoolean(), Text.literal("Water Skin should NOT be empty!"));
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.complete();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/warped_dry"
    )
    public void using_water_skin_on_dry_warped_lily_does_not_fill_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, SItems.WATER_SKIN.getDefaultStack());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getStackInHand(Hand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                Text.literal("Newly created Water Skin should be empty")
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), Text.literal("Water Skin should be empty!"));
        context.expectBlock(SBlocks.WARPED_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.complete();
    }

}
