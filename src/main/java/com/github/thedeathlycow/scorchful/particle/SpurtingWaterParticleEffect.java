package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public class SpurtingWaterParticleEffect implements ParticleOptions {

    public static final MapCodec<SpurtingWaterParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.NON_NEGATIVE_INT
                                    .fieldOf("delay")
                                    .forGetter(SpurtingWaterParticleEffect::getDelay)
                    )
                    .apply(instance, SpurtingWaterParticleEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SpurtingWaterParticleEffect> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
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
