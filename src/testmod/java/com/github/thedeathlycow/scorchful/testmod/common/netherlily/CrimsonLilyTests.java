package com.github.thedeathlycow.scorchful.testmod.common.netherlily;

import com.github.thedeathlycow.scorchful.block.CrimsonLilyBlock;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class CrimsonLilyTests {

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_player(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        PlayerEntity player = context.createMockSurvivalPlayer();

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.onEntityCollision(context.getWorld(), context.getAbsolutePos(lilyPos), player);

        context.assertTrue(isPlayerWet.getAsBoolean(), "Player should be wet");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_dry"
    )
    public void stepping_on_dry_crimson_lily_does_not_soak_player(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

        PlayerEntity player = context.createMockSurvivalPlayer();

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.onEntityCollision(context.getWorld(), context.getAbsolutePos(lilyPos), player);

        context.assertFalse(isPlayerWet.getAsBoolean(), "Player should NOT be wet");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_partially_wet"
    )
    public void stepping_on_partially_wet_crimson_lily_does_not_soak_player(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);

        PlayerEntity player = context.createMockSurvivalPlayer();

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.onEntityCollision(context.getWorld(), context.getAbsolutePos(lilyPos), player);

        context.assertFalse(isPlayerWet.getAsBoolean(), "Player should NOT be wet");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_and_hurts_strider(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        StriderEntity strider = context.spawnEntity(EntityType.STRIDER, 0, 2, 0);

        BooleanSupplier isStriderWet = strider::thermoo$isWet;

        context.assertFalse(isStriderWet.getAsBoolean(), "Newly created Strider should be dry");
        context.expectEntityWithData(
                new BlockPos(0, 2, 0),
                EntityType.STRIDER,
                StriderEntity::getHealth,
                strider.getMaxHealth()
        );

        context.startMovingTowards(strider, lilyPos, 10.0f);

        context.addInstantFinalTask(
                () -> {
                    context.assertTrue(isStriderWet.getAsBoolean(), "Strider should be wet");

                    context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
                    context.expectBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

                    context.assertTrue(
                            strider.getHealth() < strider.getMaxHealth(),
                            "Strider should have been damaged"
                    );
                    strider.setAiDisabled(true);
                }
        );
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_bottle_on_dry_crimson_lily_saturates_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        PlayerEntity player = context.createMockSurvivalPlayer();
        player.setStackInHand(Hand.MAIN_HAND, Items.POTION.getDefaultStack());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should have a water bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), "Player should NOT have a water bottle");
        context.assertTrue(playerHasGlassBottle.getAsBoolean(), "Player should have a glass bottle");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_skin_on_dry_crimson_lily_saturates_it(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        PlayerEntity player = context.createMockSurvivalPlayer();
        ItemStack filledWaterSkin = SItems.WATER_SKIN.getDefaultStack();
        ((WaterSkinItem)SItems.WATER_SKIN).addDrinks(filledWaterSkin, 1);
        player.setStackInHand(Hand.MAIN_HAND, filledWaterSkin);

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getStackInHand(Hand.MAIN_HAND));
        };

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                "Water skin should NOT be empty"
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), "Water skin should NOT be empty");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.complete();
    }

    @GameTest(
            templateName = "scorchful-test:nether_lily/crimson_wet"
    )
    public void using_water_bottle_on_wet_crimson_lily_does_not_consume_bottle(TestContext context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        PlayerEntity player = context.createMockSurvivalPlayer();
        player.setStackInHand(Hand.MAIN_HAND, Items.POTION.getDefaultStack());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().containsAny(stack -> stack.isOf(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should have a water bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), "Player should have a water bottle");
        context.assertFalse(playerHasGlassBottle.getAsBoolean(), "Player should NOT have a glass bottle");
        context.expectBlock(SBlocks.CRIMSON_LILY, lilyPos);
        context.expectBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.complete();
    }

}
