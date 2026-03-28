package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.thermoo.api.core.v2.ThermooCodecs;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.entity.LivingEntity;

public record SoundTemperatureEffect(
        SoundEvent sound,
        SoundSource category,
        boolean onlyPlayToSource,
        FloatProvider volume,
        FloatProvider pitch
) implements TemperatureEffect {
    public static final MapCodec<SoundTemperatureEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.SOUND_EVENT.byNameCodec()
                            .fieldOf("sound")
                            .forGetter(SoundTemperatureEffect::sound),
                    ThermooCodecs.createEnumCodec(SoundSource.class)
                            .fieldOf("category")
                            .orElse(SoundSource.MASTER)
                            .forGetter(SoundTemperatureEffect::category),
                    Codec.BOOL
                            .fieldOf("only_play_to_source")
                            .orElse(false)
                            .forGetter(SoundTemperatureEffect::onlyPlayToSource),
                    FloatProviders.CODEC
                            .fieldOf("volume")
                            .forGetter(SoundTemperatureEffect::volume),
                    FloatProviders.CODEC
                            .fieldOf("pitch")
                            .forGetter(SoundTemperatureEffect::pitch)
            ).apply(instance, SoundTemperatureEffect::new)
    );

    private boolean playSoundToSource(LivingEntity victim, ServerLevel level) {
        if (victim instanceof ServerPlayer serverPlayer) {
            var random = victim.getRandom();
            ServerPlayNetworking.send(
                    serverPlayer,
                    new TemperatureSoundEventPacket(
                            this.sound,
                            this.category,
                            this.volume.sample(random),
                            this.pitch.sample(random),
                            level.getSeed()
                    )
            );
            return true;
        }

        return false;
    }

    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        if (target.level() instanceof ServerLevel serverLevel) {
            if (this.onlyPlayToSource) {
                return this.playSoundToSource(target, serverLevel);
            } else {
                target.playSound(
                        this.sound,
                        this.volume.sample(serverLevel.getRandom()),
                        this.pitch.sample(serverLevel.getRandom())
                );
                return true;
            }
        }

        return false;
    }

    @Override
    public MapCodec<SoundTemperatureEffect> codec() {
        return CODEC;
    }
}
