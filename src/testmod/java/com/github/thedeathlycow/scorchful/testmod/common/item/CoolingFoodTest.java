package com.github.thedeathlycow.scorchful.testmod.common.item;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


@SuppressWarnings("unused")
public class CoolingFoodTest {

    private static final String TEMPERATURE_PROPERTY = "Temperature";

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void consume_cooling_steak_applies_cooling(TestContext context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.COOKED_BEEF;

        ServerPlayerEntity mockPlayer = createMockPlayer();
        ItemStack steak = createFoodStack(testItem);

        context.assertTrue(steak.isIn(SItemTags.IS_COOLING_FOOD), "Steak SHOULD be cooling food");

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(steak, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.atLeastOnce())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void consume_not_cooling_pork_does_not_apply_cooling(TestContext context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.PORKCHOP;

        ServerPlayerEntity mockPlayer = createMockPlayer();
        ItemStack porkchop = createFoodStack(testItem);

        context.assertFalse(porkchop.isIn(SItemTags.IS_COOLING_FOOD), "Porkchop should NOT be cooling food");

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(porkchop, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.never())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        context.complete();
    }

    @NotNull
    private static ItemStack createFoodStack(Item testItem) {
        ItemStack stack = testItem.getDefaultStack();
        stack.set(
                DataComponentTypes.FOOD,
                new FoodComponent.Builder()
                        .nutrition(1)
                        .saturationModifier(1)
                        .alwaysEdible()
                        .build()
        );
        return stack;
    }

    private static ServerPlayerEntity createMockPlayer() {
        ServerPlayerEntity mockPlayer = Mockito.mock(ServerPlayerEntity.class);
        Mockito.doNothing()
                .when(mockPlayer)
                .thermoo$addTemperature(
                        ArgumentMatchers.intThat(temp -> temp < 0),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        return mockPlayer;
    }
}
