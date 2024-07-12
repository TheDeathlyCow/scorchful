package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.Locale;

public class SpurtingWaterParticleEffect implements ParticleEffect {

    public static final MapCodec<SpurtingWaterParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codecs.NONNEGATIVE_INT
                                    .fieldOf("delay")
                                    .forGetter(SpurtingWaterParticleEffect::getDelay)
                    )
                    .apply(instance, SpurtingWaterParticleEffect::new)
    );
    public static final PacketCodec<RegistryByteBuf, SpurtingWaterParticleEffect> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            SpurtingWaterParticleEffect::getDelay,
            SpurtingWaterParticleEffect::new
    );

    private final int delay;

    public SpurtingWaterParticleEffect(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public ParticleType<SpurtingWaterParticleEffect> getType() {
        return SParticleTypes.SPURTING_WATER;
    }

}
