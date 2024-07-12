package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AbstractDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;

public class DustGrainParticle extends AbstractDustParticle<DustGrainParticleEffect> {
    public DustGrainParticle(
            ClientWorld world,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            DustGrainParticleEffect parameters,
            SpriteProvider spriteProvider
    ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, parameters, spriteProvider);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.gravityStrength = 1e-1f;

        float multiplier = this.random.nextFloat() * 0.4F + 0.6F;
        this.red = this.darken(parameters.getColor().x(), multiplier);
        this.green = this.darken(parameters.getColor().y(), multiplier);
        this.blue = this.darken(parameters.getColor().z(), multiplier);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DustGrainParticleEffect> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(
                DustGrainParticleEffect parameters,
                ClientWorld world,
                double x, double y, double z,
                double velocityX, double velocityY, double velocityZ
        ) {
            return new DustGrainParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ,
                    parameters,
                    this.spriteProvider
            );
        }
    }
}
