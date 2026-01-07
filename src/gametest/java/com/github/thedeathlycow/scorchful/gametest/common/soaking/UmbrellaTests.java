package com.github.thedeathlycow.scorchful.gametest.common.soaking;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class UmbrellaTests {
    @GameTest(
            skyAccess = true,
            environment = "scorchful-test:rainy_night"
    )
    public void holding_leather_in_mainhand_blocks_rain(TestContext context) {
        var pos = new BlockPos(1, 0, 1);
        ZombieEntity zombie = context.spawnMob(EntityType.ZOMBIE, pos);

        zombie.setStackInHand(Hand.MAIN_HAND, Items.LEATHER.getDefaultStack());
        zombie.thermoo$setWetTicks(0);

        context.waitAndRun(20L, () -> {
            context.expectEntityWithData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);
            context.complete();
        });
    }

    @GameTest(
            skyAccess = true,
            environment = "scorchful-test:rainy_night"
    )
    public void holding_leather_in_offhand_blocks_rain(TestContext context) {
        var pos = new BlockPos(1, 0, 1);
        ZombieEntity zombie = context.spawnMob(EntityType.ZOMBIE, pos);

        zombie.setStackInHand(Hand.OFF_HAND, Items.LEATHER.getDefaultStack());
        zombie.thermoo$setWetTicks(0);

        context.waitAndRun(20L, () -> {
            context.expectEntityWithData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);
            context.complete();
        });
    }
}