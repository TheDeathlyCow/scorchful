package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;

public class SpurtingWaterParticle extends SpriteBillboardParticle {

    private static final float STARTING_Y_SPEED = 30f;

    private final int delay;

    protected SpurtingWaterParticle(
            ClientWorld clientWorld,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            int delay
    ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
        this.gravityStrength = 0.75f;
        this.maxAge += delay;
        this.delay = delay;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == delay) {
            this.velocityY = STARTING_Y_SPEED;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SpurtingWaterParticleEffect> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SpurtingWaterParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            var particle = new SpurtingWaterParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ,
                    parameters.getDelay()
            );
            particle.setSprite(this.spriteProvider);
            particle.setColor(0.2f, 0.3f, 1.0f);
            return particle;
        }
    }
}
