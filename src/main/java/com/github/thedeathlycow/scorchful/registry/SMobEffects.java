package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.entity.effect.FearEffect;
import com.github.thedeathlycow.scorchful.entity.effect.HeatStrokeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SMobEffects {

    public static final Holder<MobEffect> HEAT_STROKE = register(
            "heat_stroke",
            new HeatStrokeEffect(MobEffectCategory.HARMFUL, 0xff1500)
    );

    public static final Holder<MobEffect> FEAR = register(
            "fear",
            new FearEffect(MobEffectCategory.HARMFUL, 0x510359)
                    .addAttributeModifier(Attributes.ARMOR, Scorchful.id("fear_effect/armor"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, Scorchful.id("fear_effect/armor_toughness"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful mob effects");
    }

    private static Holder<MobEffect> register(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Scorchful.id(name), statusEffect);
    }

    private SMobEffects() {

    }
}
