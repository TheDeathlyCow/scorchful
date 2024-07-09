package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.effect.HeatStrokeEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class SStatusEffects {

    public static final RegistryEntry<StatusEffect> HEAT_STROKE = register(
            "heat_stroke",
            new HeatStrokeEffect(StatusEffectCategory.HARMFUL, 0xff1500)
    );

    public static void initialize() {
        // loads the class
    }

    private static RegistryEntry<StatusEffect> register(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Scorchful.id(name), statusEffect);
    }

    private SStatusEffects() {

    }
}
