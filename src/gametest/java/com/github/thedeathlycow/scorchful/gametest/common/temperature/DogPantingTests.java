package com.github.thedeathlycow.scorchful.gametest.common.temperature;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class DogPantingTests {
    @GameTest()
    public void warm_dog_cools_down(TestContext context) {
        WolfEntity wolf = context.spawnMob(EntityType.WOLF, new BlockPos(1, 1, 1));
        wolf.thermoo$setTemperature(wolf.thermoo$getMaxTemperature());

        context.waitAndRun(20L, () -> {
            context.assertTrue(wolf.thermoo$getTemperature() < wolf.thermoo$getMaxTemperature(), Text.literal("Wolf has not cooled"));
            context.complete();
        });
    }
}