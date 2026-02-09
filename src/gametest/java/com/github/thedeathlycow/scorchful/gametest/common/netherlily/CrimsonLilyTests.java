package com.github.thedeathlycow.scorchful.gametest.common.netherlily;

import com.github.thedeathlycow.scorchful.block.CrimsonLilyBlock;
import com.github.thedeathlycow.scorchful.block.NetherLilyBlock;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class CrimsonLilyTests {

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), Component.literal("Newly created player should be dry"));

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player, InsideBlockEffectApplier.NOOP, true);

        context.assertTrue(isPlayerWet.getAsBoolean(), Component.literal("Player should be wet"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_dry"
    )
    public void stepping_on_dry_crimson_lily_does_not_soak_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), Component.literal("Newly created player should be dry"));

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player, InsideBlockEffectApplier.NOOP, true);

        context.assertFalse(isPlayerWet.getAsBoolean(), Component.literal("Player should NOT be wet"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_partially_wet"
    )
    public void stepping_on_partially_wet_crimson_lily_does_not_soak_player(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        BooleanSupplier isPlayerWet = player::thermoo$isWet;

        context.assertFalse(isPlayerWet.getAsBoolean(), Component.literal("Newly created player should be dry"));

        BlockState lilyState = context.getBlockState(lilyPos);
        lilyState.entityInside(context.getLevel(), context.absolutePos(lilyPos), player, InsideBlockEffectApplier.NOOP, true);

        context.assertFalse(isPlayerWet.getAsBoolean(), Component.literal("Player should NOT be wet"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, 2);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_wet"
    )
    public void stepping_on_wet_crimson_lily_soaks_and_hurts_strider(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MAX_LEVEL);

        Strider strider = context.spawn(EntityType.STRIDER, 0, 1, 0);

        BooleanSupplier isStriderWet = strider::thermoo$isWet;

        context.assertFalse(isStriderWet.getAsBoolean(), Component.literal("Newly created Strider should be dry"));
        context.assertEntityData(
                new BlockPos(0, 2, 0),
                EntityType.STRIDER,
                Strider::getHealth,
                strider.getMaxHealth()
        );

        context.walkTo(strider, lilyPos, 10.0f);

        context.succeedWhen(
                () -> {
                    context.assertTrue(isStriderWet.getAsBoolean(), Component.literal("Strider should be wet"));

                    context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
                    context.assertBlockProperty(lilyPos, CrimsonLilyBlock.WATER_SATURATION_LEVEL, CrimsonLilyBlock.MIN_LEVEL);

                    context.assertTrue(
                            strider.getHealth() < strider.getMaxHealth(),
                            Component.literal("Strider should have been damaged")
                    );
                    strider.setNoAi(true);
                }
        );
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_bottle_on_dry_crimson_lily_saturates_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.POTION.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
               Component.literal( "Newly created player should have a water bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(playerHasWaterBottle.getAsBoolean(), Component.literal("Player should NOT have a water bottle"));
        context.assertTrue(playerHasGlassBottle.getAsBoolean(), Component.literal("Player should have a glass bottle"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_dry"
    )
    public void using_water_skin_on_dry_crimson_lily_saturates_it(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MIN_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack filledWaterSkin = SItems.WATER_SKIN.getDefaultInstance();
        DrinkContainer.addDrinks(filledWaterSkin, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, filledWaterSkin);

        BooleanSupplier isWaterSkinEmpty = () -> {
            return !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));
        };

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                Component.literal("Water skin should NOT be empty")
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(isWaterSkinEmpty.getAsBoolean(), Component.literal("Water skin should NOT be empty"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_wet"
    )
    public void using_water_bottle_on_wet_crimson_lily_does_not_consume_bottle(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.POTION.getDefaultInstance());

        BooleanSupplier playerHasWaterBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.POTION));
        BooleanSupplier playerHasGlassBottle = () -> player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLASS_BOTTLE));

        context.assertTrue(
                playerHasWaterBottle.getAsBoolean(),
                Component.literal("Newly created player should have a water bottle")
        );

        context.useBlock(lilyPos, player);

        context.assertTrue(playerHasWaterBottle.getAsBoolean(), Component.literal("Player should have a water bottle"));
        context.assertFalse(playerHasGlassBottle.getAsBoolean(), Component.literal("Player should NOT have a glass bottle"));
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

    @GameTest(
            structure = "scorchful-test:nether_lily/crimson_wet"
    )
    public void using_water_skin_on_wet_crimson_lily_does_not_consume_skin(GameTestHelper context) {
        final BlockPos lilyPos = new BlockPos(2, 1, 2);
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);

        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        var waterSkin = SItems.WATER_SKIN.getDefaultInstance();

        DrinkContainer.addDrinks(waterSkin, 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, waterSkin);

        BooleanSupplier isWaterSkinEmpty = () -> !WaterSkinItem.hasDrink(player.getItemInHand(InteractionHand.MAIN_HAND));

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                Component.literal("Water skin should not start as empty")
        );

        context.useBlock(lilyPos, player);

        context.assertFalse(
                isWaterSkinEmpty.getAsBoolean(),
                Component.literal("Water skin should not be empty")
        );
        context.assertBlockPresent(SBlocks.CRIMSON_LILY, lilyPos);
        context.assertBlockProperty(lilyPos, NetherLilyBlock.WATER_SATURATION_LEVEL, NetherLilyBlock.MAX_LEVEL);
        context.succeed();
    }

}
