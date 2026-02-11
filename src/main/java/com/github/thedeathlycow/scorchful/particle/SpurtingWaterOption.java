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

public class SpurtingWaterOption implements ParticleOptions {

    public static final MapCodec<SpurtingWaterOption> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.NON_NEGATIVE_INT
                                    .fieldOf("delay")
                                    .forGetter(SpurtingWaterOption::getDelay)
                    )
                    .apply(instance, SpurtingWaterOption::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SpurtingWaterOption> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SpurtingWaterOption::getDelay,
            SpurtingWaterOption::new
    );

    private final int delay;

    public SpurtingWaterOption(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public ParticleType<SpurtingWaterOption> getType() {
        return SParticleTypes.SPURTING_WATER;
    }

}
