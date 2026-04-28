package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.thermoo.api.ThermooCodecs;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.LivingEntity;

public class SoundTemperatureEffect extends TemperatureEffect<SoundTemperatureEffect.Config> {

    /**
     * @param configCodec Codec for the config type
     */
    public SoundTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerLevel serverWorld, Config config) {
        if (config.onlyPlayToSource) {
            this.playSoundToSource(victim, serverWorld, config);
        } else {
            victim.playSound(
                    config.sound,
                    config.volume.sample(serverWorld.random),
                    config.pitch.sample(serverWorld.random)
            );
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.tickCount % config.interval == 0;
    }

    private void playSoundToSource(LivingEntity victim, ServerLevel world, Config config) {
        if (victim instanceof ServerPlayer serverPlayer) {
            var random = victim.getRandom();

            ServerPlayNetworking.send(
                    serverPlayer,
                    new TemperatureSoundEventPacket(
                            config.sound,
                            config.category,
                            config.volume.sample(random),
                            config.pitch.sample(random),
                            world.getSeed()
                    )
            );
        }
    }

    public record Config(
            SoundEvent sound,
            SoundSource category,
            boolean onlyPlayToSource,
            FloatProvider volume,
            FloatProvider pitch,
            int interval
    ) {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.SOUND_EVENT.byNameCodec()
                                .fieldOf("sound")
                                .forGetter(Config::sound),
                        ThermooCodecs.createEnumCodec(SoundSource.class)
                                .fieldOf("category")
                                .orElse(SoundSource.MASTER)
                                .forGetter(Config::category),
                        Codec.BOOL
                                .fieldOf("only_play_to_source")
                                .orElse(false)
                                .forGetter(Config::onlyPlayToSource),
                        FloatProvider.CODEC
                                .fieldOf("volume")
                                .forGetter(Config::volume),
                        FloatProvider.CODEC
                                .fieldOf("pitch")
                                .forGetter(Config::pitch),
                        Codec.INT
                                .fieldOf("interval")
                                .forGetter(Config::interval)
                ).apply(instance, Config::new)
        );
    }

}
