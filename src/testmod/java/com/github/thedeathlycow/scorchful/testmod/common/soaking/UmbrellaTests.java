package com.github.thedeathlycow.scorchful.testmod.common.soaking;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class UmbrellaTests {
    @GameTest(
            templateName = FabricGameTest.EMPTY_STRUCTURE,
            skyAccess = true,
            batchId = "scorchful.rainy_night"
    )
    public void holding_leather_in_mainhand_blocks_rain(TestContext context) {
        context.getWorld().setWeather(0, 1000, true, false);
        long time = context.getWorld().getTimeOfDay();
        context.setTime(18_000);

        var pos = new BlockPos(1, 1, 1);
        ZombieEntity zombie = context.spawnMob(EntityType.ZOMBIE, pos);

        zombie.setStackInHand(Hand.MAIN_HAND, Items.LEATHER.getDefaultStack());
        zombie.thermoo$setWetTicks(0);

        context.waitAndRun(20L, () -> {
            context.expectEntityWithData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);

            context.setTime(0);
            context.getWorld().resetWeather();
            context.complete();
        });
    }

    @GameTest(
            templateName = FabricGameTest.EMPTY_STRUCTURE,
            skyAccess = true,
            batchId = "scorchful.rainy_night"
    )
    public void holding_leather_in_offhand_blocks_rain(TestContext context) {
        context.getWorld().setWeather(0, 1000, true, false);
        long time = context.getWorld().getTimeOfDay();
        context.setTime(18_000);

        var pos = new BlockPos(1, 1, 1);
        ZombieEntity zombie = context.spawnMob(EntityType.ZOMBIE, pos);

        zombie.setStackInHand(Hand.OFF_HAND, Items.LEATHER.getDefaultStack());
        zombie.thermoo$setWetTicks(0);

        context.waitAndRun(20L, () -> {
            context.expectEntityWithData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);

            context.setTime(0);
            context.getWorld().resetWeather();
            context.complete();
        });
    }
}