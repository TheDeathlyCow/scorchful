package com.github.thedeathlycow.scorchful.gametest.common.item;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


@SuppressWarnings("unused")
public class CoolingFoodTest {

    private static final String TEMPERATURE_PROPERTY = "Temperature";

    @GameTest()
    public void consumeCoolingSteakAppliesCooling(GameTestHelper helper) {
        final int originalTemperature = 6300;
        final Item testItem = Items.COOKED_BEEF;

        Player mockPlayer = Mockito.spy(helper.makeMockPlayer(GameType.SURVIVAL));
        ItemStack steak = createFoodStack(testItem);

        helper.assertTrue(steak.is(SItemTags.IS_COOLING_FOOD), Component.literal("Steak SHOULD be cooling food"));

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(steak, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.atLeastOnce())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(TemperatureChange.class)
                );
        helper.succeed();
    }

    @GameTest()
    public void consumeNonCoolingPorkDoesNotApplyCooling(GameTestHelper helper) {
        final int originalTemperature = 6300;
        final Item testItem = Items.PORKCHOP;

        Player mockPlayer = Mockito.spy(helper.makeMockPlayer(GameType.SURVIVAL));
        ItemStack porkchop = createFoodStack(testItem);

        helper.assertFalse(porkchop.is(SItemTags.IS_COOLING_FOOD), Component.literal("Porkchop should NOT be cooling food"));

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(porkchop, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.never())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(TemperatureChange.class)
                );
        helper.succeed();
    }

    @NotNull
    private static ItemStack createFoodStack(Item testItem) {
        ItemStack stack = testItem.getDefaultInstance();
        stack.set(
                DataComponents.FOOD,
                new FoodProperties.Builder()
                        .nutrition(1)
                        .saturationModifier(1)
                        .alwaysEdible()
                        .build()
        );
        return stack;
    }
}
