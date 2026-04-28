package com.github.thedeathlycow.scorchful.testmod.common.temperature;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;

@SuppressWarnings("unused")
public class DogPantingTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void warm_dog_cools_down(GameTestHelper context) {
        Wolf wolf = context.spawnWithNoFreeWill(EntityType.WOLF, new BlockPos(1, 1, 1));
        wolf.thermoo$setTemperature(wolf.thermoo$getMaxTemperature());

        context.runAfterDelay(20L, () -> {
            context.assertTrue(wolf.thermoo$getTemperature() < wolf.thermoo$getMaxTemperature(), "Wolf has not cooled");
            context.succeed();
        });
    }
}