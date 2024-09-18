package com.github.thedeathlycow.scorchful.testmod.common.item;

import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.GameMode;


@SuppressWarnings("unused")
public class CoolingFoodTest {

    private static final String TEMPERATURE_PROPERTY = "Temperature";

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void consume_cooling_steak_applies_cooling(TestContext context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.COOKED_BEEF;

        PlayerEntity mockPlayer = context.createMockPlayer(GameMode.SURVIVAL);
        mockPlayer.thermoo$setTemperature(originalTemperature);
        context.testEntityProperty(mockPlayer, TemperatureAware::thermoo$getTemperature, TEMPERATURE_PROPERTY, 6300);

        ItemStack steak = testItem.getDefaultStack();
        steak.set(DataComponentTypes.FOOD, new FoodComponent.Builder().alwaysEdible().build());
        context.assertTrue(steak.isIn(SItemTags.IS_COOLING_FOOD), "Steak SHOULD be cooling food");

        mockPlayer.tryEatFood(context.getWorld(), steak);

        context.testEntity(mockPlayer, p -> p.thermoo$getTemperature() < originalTemperature, "Cooling food reduced temperature");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void consume_not_cooling_pork_does_not_apply_cooling(TestContext context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.PORKCHOP;

        PlayerEntity mockPlayer = context.createMockPlayer(GameMode.SURVIVAL);
        mockPlayer.thermoo$setTemperature(originalTemperature);
        context.testEntityProperty(mockPlayer, TemperatureAware::thermoo$getTemperature, TEMPERATURE_PROPERTY, 6300);

        ItemStack porkchop = testItem.getDefaultStack();
        porkchop.set(DataComponentTypes.FOOD, new FoodComponent.Builder().alwaysEdible().build());
        context.assertFalse(porkchop.isIn(SItemTags.IS_COOLING_FOOD), "Porkchop should NOT be cooling food");

        mockPlayer.tryEatFood(context.getWorld(), porkchop);

        context.testEntityProperty(mockPlayer, TemperatureAware::thermoo$getTemperature, TEMPERATURE_PROPERTY, 6300);
        context.complete();
    }
}
