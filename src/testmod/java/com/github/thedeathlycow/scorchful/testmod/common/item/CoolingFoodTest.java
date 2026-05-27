package com.github.thedeathlycow.scorchful.testmod.common.item;

import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.scorchful.testmod.common.ScorchfulTestMod;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


@SuppressWarnings("unused")
@GameTestHolder(ScorchfulTestMod.MODID)
@PrefixGameTestTemplate(false)
public class CoolingFoodTest {

    private static final String TEMPERATURE_PROPERTY = "Temperature";

    @GameTest(template = ScorchfulTestMod.EMPTY_STRUCTURE)
    public void consume_cooling_steak_applies_cooling(GameTestHelper context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.COOKED_BEEF;

        ServerPlayer mockPlayer = createMockPlayer();
        ItemStack steak = createFoodStack(testItem);

        context.assertTrue(steak.is(SItemTags.IS_COOLING_FOOD), "Steak SHOULD be cooling food");

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(steak, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.atLeastOnce())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        context.succeed();
    }

    @GameTest(template = ScorchfulTestMod.EMPTY_STRUCTURE)
    public void consume_not_cooling_pork_does_not_apply_cooling(GameTestHelper context) {
        final int originalTemperature = 6300;
        final Item testItem = Items.PORKCHOP;

        ServerPlayer mockPlayer = createMockPlayer();
        ItemStack porkchop = createFoodStack(testItem);

        context.assertFalse(porkchop.is(SItemTags.IS_COOLING_FOOD), "Porkchop should NOT be cooling food");

        ScorchfulItemEvents.CONSUME_ITEM.invoker().consume(porkchop, mockPlayer);

        Mockito.verify(mockPlayer, Mockito.never())
                .thermoo$addTemperature(
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        context.succeed();
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

    private static ServerPlayer createMockPlayer() {
        ServerPlayer mockPlayer = Mockito.mock(ServerPlayer.class);
        Mockito.doNothing()
                .when(mockPlayer)
                .thermoo$addTemperature(
                        ArgumentMatchers.intThat(temp -> temp < 0),
                        ArgumentMatchers.any(HeatingModes.class)
                );
        return mockPlayer;
    }
}
