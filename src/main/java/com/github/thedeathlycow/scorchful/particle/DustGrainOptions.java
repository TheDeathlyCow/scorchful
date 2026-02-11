package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3fc;

public class DustGrainOptions extends ScalableParticleOptionsBase {

    public static final MapCodec<DustGrainOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.VECTOR3F
                                    .fieldOf("color")
                                    .forGetter(DustGrainOptions::getColor),
                            SCALE
                                    .fieldOf("scale")
                                    .forGetter(DustGrainOptions::getScale)
                    )
                    .apply(instance, DustGrainOptions::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DustGrainOptions> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            DustGrainOptions::getColor,
            ByteBufCodecs.FLOAT,
            DustGrainOptions::getScale,
            DustGrainOptions::new
    );

    private final Vector3fc color;

    public DustGrainOptions(Vector3fc color, float scale) {
        super(scale);
        this.color = color;
    }

    @Override
    public ParticleType<DustGrainOptions> getType() {
        return SParticleTypes.DUST_GRAIN;
    }

    public Vector3fc getColor() {
        return color;
    }
}
