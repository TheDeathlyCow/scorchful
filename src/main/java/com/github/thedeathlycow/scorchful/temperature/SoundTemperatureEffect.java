package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffect;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProvider;

public class SoundTemperatureEffect extends TemperatureEffect<SoundTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        if (config.onlyPlayToSource) {
            this.playSoundToSource(victim, serverWorld, config);
        } else {
            victim.playSound(
                    config.sound.value(),
                    config.volume.get(serverWorld.random),
                    config.pitch.get(serverWorld.random)
            );
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.interval == 0;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json);
    }

    private void playSoundToSource(LivingEntity victim, ServerWorld world, Config config) {
        if (victim instanceof ServerPlayerEntity serverPlayer) {
            var packet = new PlaySoundS2CPacket(
                    config.sound,
                    config.category,
                    victim.getX(), victim.getY(), victim.getZ(),
                    config.volume.get(world.random),
                    config.pitch.get(world.random),
                    world.getSeed()
            );

            serverPlayer.networkHandler.sendPacket(packet);
        }
    }

    public record Config(
            RegistryEntry<SoundEvent> sound,
            SoundCategory category,
            boolean onlyPlayToSource,
            FloatProvider volume,
            FloatProvider pitch,
            int interval
    ) {
        public static Config fromJson(JsonElement json) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            RegistryEntry<SoundEvent> sound = RegistryEntry.of(
                    SoundEvent.of(
                            new Identifier(object.get("sound").getAsString())
                    )
            );

            SoundCategory category = object.has("category")
                    ? SoundCategory.valueOf(object.get("category").getAsString().toLowerCase())
                    : SoundCategory.MASTER;


            boolean onlyPlayToSource = object.has("only_play_to_source")
                    && object.get("only_play_to_source").getAsBoolean();


            FloatProvider volume = FloatProvider.VALUE_CODEC.parse(JsonOps.INSTANCE, object.get("volume"))
                    .getOrThrow(false, message -> {
                        throw new JsonSyntaxException(message);
                    });

            FloatProvider pitch = FloatProvider.VALUE_CODEC.parse(JsonOps.INSTANCE, object.get("pitch"))
                    .getOrThrow(false, message -> {
                        throw new JsonSyntaxException(message);
                    });

            int interval = object.get("interval").getAsInt();

            return new Config(sound, category, onlyPlayToSource, volume, pitch, interval);
        }
    }

}
