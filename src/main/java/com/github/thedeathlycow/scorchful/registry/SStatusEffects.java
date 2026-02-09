package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.effect.FearStatusEffect;
import com.github.thedeathlycow.scorchful.entity.effect.HeatStrokeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SStatusEffects {

    public static final Holder<MobEffect> HEAT_STROKE = register(
            "heat_stroke",
            new HeatStrokeEffect(MobEffectCategory.HARMFUL, 0xff1500)
    );

    public static final Holder<MobEffect> FEAR = register(
            "fear",
            new FearStatusEffect(MobEffectCategory.HARMFUL, 0x510359)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful status effects");
    }

    private static Holder<MobEffect> register(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Scorchful.id(name), statusEffect);
    }

    private SStatusEffects() {

    }
}
