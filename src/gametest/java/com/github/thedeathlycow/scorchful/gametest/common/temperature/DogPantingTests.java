package com.github.thedeathlycow.scorchful.gametest.common.temperature;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.wolf.Wolf;

@SuppressWarnings("unused")
public class DogPantingTests {
    @GameTest()
    public void warm_dog_cools_down(GameTestHelper context) {
        Wolf wolf = context.spawnWithNoFreeWill(EntityType.WOLF, new BlockPos(1, 1, 1));
        wolf.thermoo$setTemperature(wolf.thermoo$getMaxTemperature());

        context.runAfterDelay(20L, () -> {
            context.assertTrue(wolf.thermoo$getTemperature() < wolf.thermoo$getMaxTemperature(), Component.literal("Wolf has not cooled"));
            context.succeed();
        });
    }
}