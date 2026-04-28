package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticleBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;

public class DustGrainParticle extends DustParticleBase<DustGrainParticleEffect> {
    public DustGrainParticle(
            ClientLevel world,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            DustGrainParticleEffect parameters,
            SpriteSet spriteProvider
    ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, parameters, spriteProvider);
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
        this.gravity = 1e-1f;

        float multiplier = this.random.nextFloat() * 0.4F + 0.6F;
        this.rCol = this.randomizeColor(parameters.getColor().x(), multiplier);
        this.gCol = this.randomizeColor(parameters.getColor().y(), multiplier);
        this.bCol = this.randomizeColor(parameters.getColor().z(), multiplier);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<DustGrainParticleEffect> {

        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(
                DustGrainParticleEffect parameters,
                ClientLevel world,
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
