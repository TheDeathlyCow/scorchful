package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.Nullable;

public class SpurtingWaterParticle extends TextureSheetParticle {

    private static final float STARTING_Y_SPEED = 30f;

    private final int delay;

    protected SpurtingWaterParticle(
            ClientLevel clientWorld,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            int delay
    ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
        this.gravity = 0.75f;
        this.lifetime += delay;
        this.delay = delay;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == delay) {
            this.yd = STARTING_Y_SPEED;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SpurtingWaterParticleEffect> {

        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(
                SpurtingWaterParticleEffect parameters,
                ClientLevel world,
                double x, double y, double z,
                double velocityX, double velocityY, double velocityZ
        ) {
            var particle = new SpurtingWaterParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ,
                    parameters.getDelay()
            );
            particle.pickSprite(this.spriteProvider);
            particle.setColor(0.2f, 0.3f, 1.0f);
            return particle;
        }
    }
}
