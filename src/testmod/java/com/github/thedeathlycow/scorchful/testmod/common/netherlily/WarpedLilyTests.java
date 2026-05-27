package com.github.thedeathlycow.scorchful.testmod.common.netherlily;

import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import java.util.function.BooleanSupplier;

import com.github.thedeathlycow.scorchful.testmod.common.ScorchfulTestMod;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@SuppressWarnings("unused")
@GameTestHolder(ScorchfulTestMod.MODID)
@PrefixGameTestTemplate(false)
public class WarpedLilyTests {

    @GameTest(
            template = "nether_lily/warped_wet"
    )
    public void using_glass_bottle_on_wet_warped_lily_fills_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should not have any water bottles"
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                "Newly created player should have a glass bottle"
        );


        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), "Player should have a water bottle");
        context.assertFalse(
                playerHasGlassBottle.getAsBoolean(),
                "Player should NOT have a glass bottle"
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "nether_lily/warped_dry"
    )
    public void using_glass_bottle_on_dry_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should not have any water bottles"
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                "Newly created player should have a glass bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), "Player should NOT have a water bottle");
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                "Player should have a glass bottle"
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "nether_lily/warped_partially_wet"
    )
    public void using_glass_bottle_on_partially_wet_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.GLASS_BOTTLE.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertFalse(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should not have any water bottles"
        );
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                "Newly created player should have a glass bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), "Player should NOT have a water bottle");
        context.assertTrue(
                playerHasGlassBottle.getAsBoolean(),
                "Player should have a glass bottle"
        );
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.succeed();
    }

    @GameTest(
            template = "nether_lily/warped_wet"
    )
    public void using_water_skin_on_wet_warped_lily_fills_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, SItems.WATER_SKIN.getDefaultInstance());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                "Newly created Water Skin should be empty"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(isWaterSkinEmpty.getAsBoolean(), "Water Skin should NOT be empty!");
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "nether_lily/warped_dry"
    )
    public void using_water_skin_on_dry_warped_lily_does_not_fill_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, SItems.WATER_SKIN.getDefaultInstance());

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertTrue(
                isWaterSkinEmpty.getAsBoolean(),
                "Newly created Water Skin should be empty"
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), "Water Skin should be empty!");
        context.assertBlockPresent(SBlocks.WARPED_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);
        context.succeed();
    }

}
