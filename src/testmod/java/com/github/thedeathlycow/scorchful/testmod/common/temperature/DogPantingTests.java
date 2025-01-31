package com.github.thedeathlycow.scorchful.testmod.common.temperature;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class DogPantingTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void warm_dog_cools_down(TestContext context) {
        WolfEntity wolf = context.spawnMob(EntityType.WOLF, new BlockPos(1, 1, 1));
        wolf.thermoo$setTemperature(wolf.thermoo$getMaxTemperature());

        context.waitAndRun(20L, () -> {
            context.assertTrue(wolf.thermoo$getTemperature() < wolf.thermoo$getMaxTemperature(), "Wolf has not cooled");
            context.complete();
        });
    }
}