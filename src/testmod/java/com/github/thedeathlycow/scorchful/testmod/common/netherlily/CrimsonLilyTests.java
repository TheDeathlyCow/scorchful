package com.github.thedeathlycow.scorchful.testmod.common.netherlily;

import com.github.thedeathlycow.scorchful.block.CrimsonLilyBlock;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public class CrimsonLilyTests {

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player);

        context.assertTrue(isPlayerWet.getAsBoolean(), "Player should be wet");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_dry"
    )
    public void stepping_on_dry_crimson_lily_does_not_soak_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player);

        context.assertFalse(isPlayerWet.getAsBoolean(), "Player should NOT be wet");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_partially_wet"
    )
    public void stepping_on_partially_wet_crimson_lily_does_not_soak_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), "Newly created player should be dry");

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player);

        context.assertFalse(isPlayerWet.getAsBoolean(), "Player should NOT be wet");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_and_hurts_strider(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        Strider strider = context.spawn(EntityType.STRIDER, 0, 2, 0);

        BooleanSupplier isStriderWet = strider::thermoo$isWet;

        context.assertFalse(isStriderWet.getAsBoolean(), "Newly created Strider should be dry");
        context.assertEntityData(
                new BlockPos(0, 2, 0),
                EntityType.STRIDER,
                Strider::getHealth,
                strider.getMaxHealth()
        );

        context.walkTo(strider, lilyPos, 10.0f);

        context.succeedWhen(
                () -> {
                    context.assertTrue(isStriderWet.getAsBoolean(), "Strider should be wet");

                    context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
                    context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

                    context.assertTrue(
                            strider.getHealth() < strider.getMaxHealth(),
                            "Strider should have been damaged"
                    );
                    strider.setNoAi(true);
                }
        );
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_bottle_on_dry_crimson_lily_saturates_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.POTION.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should have a water bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), "Player should NOT have a water bottle");
        context.assertTrue(playerHasGlassBottle.getAsBoolean(), "Player should have a glass bottle");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_skin_on_dry_crimson_lily_saturates_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack filledWaterSkin = SItems.WATER_SKIN.getDefaultInstance();
        WaterSkinItem.addDrinks(filledWaterSkin, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, filledWaterSkin);

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                "Water skin should NOT be empty"
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), "Water skin should NOT be empty");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_wet"
    )
    public void using_water_bottle_on_wet_crimson_lily_does_not_consume_bottle(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.POTION.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
                "Newly created player should have a water bottle"
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), "Player should have a water bottle");
        context.assertFalse(playerHasGlassBottle.getAsBoolean(), "Player should NOT have a glass bottle");
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            template = "scorchful-test:nether_lily/crimson_wet"
    )
    public void using_water_skin_on_wet_crimson_lily_does_not_consume_skin(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 2, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        var waterSkin = SItems.WATER_SKIN.getDefaultInstance();
        ((WaterSkinItem) SItems.WATER_SKIN).addDrinks(waterSkin, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, waterSkin);

        BooleanSupplier isWaterSkinEmpty = () -> !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                "Water skin should not start as empty"
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                "Water skin should not be empty"
        );
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

}
