package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class SpurtingWaterParticle extends SpriteBillboardParticle {
    protected SpurtingWaterParticle(
            ClientWorld clientWorld,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ
    ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
        this.gravityStrength = 1.0f;
        this.maxAge += 40;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            var particle = new SpurtingWaterParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ
            );
            particle.setSprite(this.spriteProvider);
            particle.setColor(0.2f, 0.3f, 1.0f);
            return particle;
        }
    }
}
