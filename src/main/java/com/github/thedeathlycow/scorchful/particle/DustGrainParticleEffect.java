package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.joml.Vector3f;

public class DustGrainParticleEffect extends AbstractDustParticleEffect {

    public static final Factory FACTORY = new Factory();

    public DustGrainParticleEffect(Vector3f color, float scale) {
        super(color, scale);
    }

    @Override
    public ParticleType<?> getType() {
        return SParticleTypes.DUST_GRAIN;
    }

    @SuppressWarnings("deprecation")
    public static class Factory implements ParticleEffect.Factory<DustGrainParticleEffect> {

        @Override
        public DustGrainParticleEffect read(
                ParticleType<DustGrainParticleEffect> particleType,
                StringReader stringReader
        ) throws CommandSyntaxException {
            Vector3f color = AbstractDustParticleEffect.readColor(stringReader);
            stringReader.expect(' ');
            float scale = stringReader.readFloat();
            return new DustGrainParticleEffect(color, scale);
        }

        @Override
        public DustGrainParticleEffect read(
                ParticleType<DustGrainParticleEffect> particleType,
                PacketByteBuf packetByteBuf
        ) {
            return new DustGrainParticleEffect(
                    AbstractDustParticleEffect.readColor(packetByteBuf),
                    packetByteBuf.readFloat()
            );
        }
    }
}
