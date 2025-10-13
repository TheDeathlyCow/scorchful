package com.github.thedeathlycow.scorchful.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class SpurtingWaterParticle extends AbstractSlowingParticle {
    private static final float STARTING_Y_SPEED = 30f;

    private final int delay;

    protected SpurtingWaterParticle(
            ClientWorld clientWorld,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            Sprite sprite,
            int delay
    ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ, sprite);
        this.gravityStrength = 0.75f;
        this.maxAge += delay;
        this.delay = delay;
        this.setColor(0.2f, 0.3f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == delay) {
            this.velocityY = STARTING_Y_SPEED;
        }
    }

    @Override
    protected RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_OPAQUE;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SpurtingWaterParticleEffect> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        @Nullable
        public Particle createParticle(
                SpurtingWaterParticleEffect parameters,
                ClientWorld world,
                double x, double y, double z,
                double velocityX, double velocityY, double velocityZ,
                Random random
        ) {
            return new SpurtingWaterParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ,
                    this.spriteProvider.getFirst(),
                    parameters.getDelay()
            );
        }
    }
}
