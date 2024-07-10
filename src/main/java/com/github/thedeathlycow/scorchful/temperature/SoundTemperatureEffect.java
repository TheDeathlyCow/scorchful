package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.thermoo.api.ThermooCodecs;
import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProvider;

public class SoundTemperatureEffect extends TemperatureEffect<SoundTemperatureEffect.Config> {


    public static final Identifier PACKET_ID = Scorchful.id("play_sound_temperature_effect");


    /**
     * @param configCodec Codec for the config type
     */
    public SoundTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        if (config.onlyPlayToSource) {
            this.playSoundToSource(victim, serverWorld, config);
        } else {
            victim.playSound(
                    config.sound,
                    config.volume.get(serverWorld.random),
                    config.pitch.get(serverWorld.random)
            );
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.interval == 0;
    }

    private void playSoundToSource(LivingEntity victim, ServerWorld world, Config config) {
        if (victim instanceof ServerPlayerEntity serverPlayer) {
            var random = victim.getRandom();

            ServerPlayNetworking.send(
                    serverPlayer,
                    new TemperatureSoundEventPacket(
                            config.sound,
                            config.category,
                            config.volume.get(random),
                            config.pitch.get(random),
                            world.getSeed()
                    )
            );
        }
    }

    public record Config(
            SoundEvent sound,
            SoundCategory category,
            boolean onlyPlayToSource,
            FloatProvider volume,
            FloatProvider pitch,
            int interval
    ) {

        public static final Codec<Config> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Registries.SOUND_EVENT.getCodec()
                                .fieldOf("sound")
                                .forGetter(Config::sound),
                        ThermooCodecs.createEnumCodec(SoundCategory.class)
                                .fieldOf("category")
                                .orElse(SoundCategory.MASTER)
                                .forGetter(Config::category),
                        Codec.BOOL
                                .fieldOf("only_play_to_source")
                                .orElse(false)
                                .forGetter(Config::onlyPlayToSource),
                        FloatProvider.VALUE_CODEC
                                .fieldOf("volume")
                                .forGetter(Config::volume),
                        FloatProvider.VALUE_CODEC
                                .fieldOf("pitch")
                                .forGetter(Config::pitch),
                        Codec.INT
                                .fieldOf("interval")
                                .forGetter(Config::interval)
                ).apply(instance, Config::new)
        );
    }

}
