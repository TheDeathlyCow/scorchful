package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public final class SPotions {
    private static final String PARANOIA_NAME = "paranoia";
    private static final int ONE_MINUTE = 60 * 20;

    public static final Holder<Potion> PARANOIA = registerReference(
            PARANOIA_NAME,
            create(
                    PARANOIA_NAME,
                    new MobEffectInstance(SStatusEffects.FEAR, ONE_MINUTE * 3, 0),
                    new MobEffectInstance(MobEffects.DARKNESS, ONE_MINUTE * 3, 0)
            )
    );

    public static final Holder<Potion> LONG_PARANOIA = registerReference(
            "long_" + PARANOIA_NAME,
            create(
                    PARANOIA_NAME,
                    new MobEffectInstance(SStatusEffects.FEAR, ONE_MINUTE * 8, 0),
                    new MobEffectInstance(MobEffects.DARKNESS, ONE_MINUTE * 8, 0)
            )
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful potions");
        FabricBrewingRecipeRegistryBuilder.BUILD.register(
                builder -> {
                    builder.addMix(Potions.AWKWARD, Items.WITHER_ROSE, PARANOIA);
                    builder.addMix(PARANOIA, Items.REDSTONE, LONG_PARANOIA);
                }
        );
    }

    private static Potion create(String name, MobEffectInstance... effects) {
        return new Potion(String.format("%s.%s", Scorchful.MODID, name), effects);
    }

    private static Holder<Potion> registerReference(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Scorchful.id(name), potion);
    }

    private SPotions() {

    }
}
