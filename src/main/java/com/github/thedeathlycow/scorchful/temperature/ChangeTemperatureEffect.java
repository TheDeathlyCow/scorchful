// TODO(Ravel): Failed to fully resolve file: null cannot be cast to non-null type com.intellij.psi.PsiClass
package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.LivingEntity;

public record ChangeTemperatureEffect(
        int amount,
        Holder<TemperatureSource> source
) implements TemperatureEffect {
    public static final MapCodec<ChangeTemperatureEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT
                            .fieldOf("amount")
                            .forGetter(ChangeTemperatureEffect::amount),
                    RegistryFixedCodec.create(ThermooRegistries.TEMPERATURE_SOURCE)
                            .fieldOf("interval")
                            .forGetter(ChangeTemperatureEffect::source)
            ).apply(instance, ChangeTemperatureEffect::new)
    );

    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        target.thermoo$addTemperature(this.amount, TemperatureChange.create(this.source));
        return true;
    }

    @Override
    public MapCodec<ChangeTemperatureEffect> codec() {
        return CODEC;
    }
}