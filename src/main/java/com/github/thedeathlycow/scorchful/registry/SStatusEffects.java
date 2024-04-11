package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.effect.HeatStrokeEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Colors;

public class SStatusEffects {

    public static final StatusEffect HEAT_STROKE = new HeatStrokeEffect(StatusEffectCategory.HARMFUL, Colors.RED);

    public static void registerAll() {
        register("heat_stroke", HEAT_STROKE);
    }

    private static void register(String name, StatusEffect statusEffect) {
        Registry.register(Registries.STATUS_EFFECT, Scorchful.id(name), statusEffect);
    }

    private SStatusEffects() {

    }
}
