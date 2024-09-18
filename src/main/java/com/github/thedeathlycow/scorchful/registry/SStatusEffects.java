package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.effect.FearStatusEffect;
import com.github.thedeathlycow.scorchful.entity.effect.HeatStrokeEffect;
import com.github.thedeathlycow.scorchful.entity.effect.MesmerizedStatusEffect;
import com.github.thedeathlycow.scorchful.entity.effect.MesmerizingStatusEffect;
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

    public static final RegistryEntry<StatusEffect> FEAR = register(
            "fear",
            new FearStatusEffect(StatusEffectCategory.HARMFUL, 0x510359)
    );

    public static final RegistryEntry<StatusEffect> MESMERIZED = register(
            "mesmerized",
            new MesmerizedStatusEffect(StatusEffectCategory.HARMFUL, 0x42e6f5)
    );

    public static final RegistryEntry<StatusEffect> MESMERIZING = register(
            "mesmerizing",
            new MesmerizingStatusEffect(StatusEffectCategory.BENEFICIAL, 0xfff94d)
    );

    public static void initialize() {
        MesmerizedStatusEffect.ON_APPROACHED_MESMER_TARGET.register(FearStatusEffect::onMesmerizedActivated);
    }

    private static RegistryEntry<StatusEffect> register(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Scorchful.id(name), statusEffect);
    }

    private SStatusEffects() {

    }
}
