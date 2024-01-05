package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

@Environment(EnvType.CLIENT)
public class SModelPredicates {

    public static void onInitialize() {
        ModelPredicateProviderRegistry.register(
                SItems.WATER_SKIN, Scorchful.id("is_drink_empty"),
                (stack, world, entity, seed) -> {
                    return WaterSkinItem.getNumDrinks(stack) == 0
                            ? 1.0f
                            : 0.0f;
                }
        );
    }

    private SModelPredicates() {

    }


}
