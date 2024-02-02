package com.github.thedeathlycow.scorchful.particle;

import com.github.thedeathlycow.scorchful.registry.SParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class SpurtingWaterParticleEffect implements ParticleEffect {

    public static final Factory FACTORY = new Factory();

    private final int delay;

    public SpurtingWaterParticleEffect(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public ParticleType<?> getType() {
        return SParticleTypes.SPURTING_WATER;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.delay);
    }

    @Override
    public String asString() {
        Identifier id = Registries.PARTICLE_TYPE.getId(this.getType());
        assert id != null;
        return String.format(Locale.ROOT, "%s %d", id, delay);
    }

    @SuppressWarnings("deprecation")
    public static class Factory implements ParticleEffect.Factory<SpurtingWaterParticleEffect> {

        @Override
        public SpurtingWaterParticleEffect read(
                ParticleType<SpurtingWaterParticleEffect> type,
                StringReader reader
        ) throws CommandSyntaxException {
            reader.expect(' ');
            int delay = reader.readInt();
            return new SpurtingWaterParticleEffect(delay);
        }

        @Override
        public SpurtingWaterParticleEffect read(ParticleType<SpurtingWaterParticleEffect> type, PacketByteBuf buf) {
            int delay = buf.readVarInt();
            return new SpurtingWaterParticleEffect(delay);
        }
    }

}
