package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class DustGrainParticleEffect extends ScalableParticleOptionsBase {

    public static final MapCodec<DustGrainParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.VECTOR3F
                                    .fieldOf("color")
                                    .forGetter(DustGrainParticleEffect::getColor),
                            SCALE
                                    .fieldOf("scale")
                                    .forGetter(DustGrainParticleEffect::getScale)
                    )
                    .apply(instance, DustGrainParticleEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DustGrainParticleEffect> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            DustGrainParticleEffect::getColor,
            ByteBufCodecs.FLOAT,
            DustGrainParticleEffect::getScale,
            DustGrainParticleEffect::new
    );

    private final Vector3f color;

    public DustGrainParticleEffect(Vector3f color, float scale) {
        super(scale);
        this.color = color;
    }

    @Override
    public ParticleType<DustGrainParticleEffect> getType() {
        return SParticleTypes.DUST_GRAIN;
    }

    public Vector3f getColor() {
        return color;
    }
}
