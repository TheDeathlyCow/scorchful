package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class SPotions {

    private static final String PARANOIA_NAME = "paranoia";
    private static final int ONE_MINUTE = 60 * 20;

    public static final RegistryEntry<Potion> PARANOIA = registerReference(
            PARANOIA_NAME,
            create(
                    PARANOIA_NAME,
                    new StatusEffectInstance(SStatusEffects.FEAR, ONE_MINUTE * 3, 0),
                    new StatusEffectInstance(StatusEffects.DARKNESS, ONE_MINUTE * 3, 0)
            )
    );

    public static final RegistryEntry<Potion> LONG_PARANOIA = registerReference(
            "long_" + PARANOIA_NAME,
            create(
                    PARANOIA_NAME,
                    new StatusEffectInstance(SStatusEffects.FEAR, ONE_MINUTE * 8, 0),
                    new StatusEffectInstance(StatusEffects.DARKNESS, ONE_MINUTE * 8, 0)
            )
    );

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(
                builder -> {
                    builder.registerPotionRecipe(Potions.AWKWARD, Items.WITHER_ROSE, PARANOIA);
                    builder.registerPotionRecipe(PARANOIA, Items.REDSTONE, LONG_PARANOIA);
                }
        );
    }

    private static Potion create(String name, StatusEffectInstance... effects) {
        return new Potion(String.format("%s.%s", Scorchful.MODID, name), effects);
    }

    private static RegistryEntry<Potion> registerReference(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Scorchful.id(name), potion);
    }

    private SPotions() {

    }
}
