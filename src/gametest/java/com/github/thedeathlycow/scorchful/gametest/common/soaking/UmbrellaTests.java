package com.github.thedeathlycow.scorchful.gametest.common.soaking;

import com.github.thedeathlycow.thermoo.api.core.v2.Soakable;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class UmbrellaTests {
    @GameTest(
            skyAccess = true,
            environment = "scorchful-test:rainy_night"
    )
    public void holding_leather_in_mainhand_blocks_rain(GameTestHelper context) {
        var pos = new BlockPos(1, 0, 1);
        Zombie zombie = context.spawnWithNoFreeWill(EntityType.ZOMBIE, pos);

        zombie.setItemInHand(InteractionHand.MAIN_HAND, Items.LEATHER.getDefaultInstance());
        zombie.thermoo$setWetTicks(0);

        context.runAfterDelay(20L, () -> {
            context.assertEntityData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);
            context.succeed();
        });
    }

    @GameTest(
            skyAccess = true,
            environment = "scorchful-test:rainy_night"
    )
    public void holding_leather_in_offhand_blocks_rain(GameTestHelper context) {
        var pos = new BlockPos(1, 0, 1);
        Zombie zombie = context.spawnWithNoFreeWill(EntityType.ZOMBIE, pos);

        zombie.setItemInHand(InteractionHand.OFF_HAND, Items.LEATHER.getDefaultInstance());
        zombie.thermoo$setWetTicks(0);

        context.runAfterDelay(20L, () -> {
            context.assertEntityData(pos, EntityType.ZOMBIE, Soakable::thermoo$getWetTicks, 0);
            context.succeed();
        });
    }
}