package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.mixin.accessor.WolfAccessor;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;

public record WolfPantSoundEffect(float chance) implements TemperatureEffect {
    public static final MapCodec<WolfPantSoundEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_FLOAT
                            .fieldOf("chance")
                            .forGetter(WolfPantSoundEffect::chance)
            ).apply(instance, WolfPantSoundEffect::new)
    );

    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        if (target instanceof Wolf wolf) {
            if (target.getRandom().nextFloat() < chance * target.thermoo$getTemperatureScale()) {
                Holder<SoundEvent> pantSound = ((WolfAccessor) wolf).scorchful$getSoundSet().pantSound();
                wolf.makeSound(pantSound.value());
            }
            
            return true;
        }

        return false;
    }

    @Override
    public MapCodec<WolfPantSoundEffect> codec() {
        return CODEC;
    }
}