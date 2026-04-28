package com.github.thedeathlycow.scorchful.testmod.common.soaking;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class UmbrellaTests {
    @GameTest(
            template = FabricGameTest.EMPTY_STRUCTURE,
            skyAccess = true,
            batch = "scorchful.rainy_night"
    )
    public void holding_leather_in_mainhand_blocks_rain(GameTestHelper context) {
        context.getLevel().setWeatherParameters(0, 1000, true, false);
        long time = context.getLevel().getDayTime();
        context.setDayTime(18_000);

        var pos = new BlockPos(1, 1, 1);
        Zombie zombie = context.spawnWithNoFreeWill(EntityType.ZOMBIE, pos);

        zombie.setItemInHand(InteractionHand.MAIN_HAND, Items.LEATHER.getDefaultInstance());
        zombie.thermoo$setWetTicks(0);

        context.runAfterDelay(20L, () -> {
            context.assertEntityData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);

            context.setDayTime(0);
            context.getLevel().resetWeatherCycle();
            context.succeed();
        });
    }

    @GameTest(
            template = FabricGameTest.EMPTY_STRUCTURE,
            skyAccess = true,
            batch = "scorchful.rainy_night"
    )
    public void holding_leather_in_offhand_blocks_rain(GameTestHelper context) {
        context.getLevel().setWeatherParameters(0, 1000, true, false);
        long time = context.getLevel().getDayTime();
        context.setDayTime(18_000);

        var pos = new BlockPos(1, 1, 1);
        Zombie zombie = context.spawnWithNoFreeWill(EntityType.ZOMBIE, pos);

        zombie.setItemInHand(InteractionHand.OFF_HAND, Items.LEATHER.getDefaultInstance());
        zombie.thermoo$setWetTicks(0);

        context.runAfterDelay(20L, () -> {
            context.assertEntityData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);

            context.setDayTime(0);
            context.getLevel().resetWeatherCycle();
            context.succeed();
        });
    }
}