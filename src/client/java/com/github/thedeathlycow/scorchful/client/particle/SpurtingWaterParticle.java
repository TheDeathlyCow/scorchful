package com.github.thedeathlycow.scorchful.client.particle;

import com.github.thedeathlycow.scorchful.particle.SpurtingWaterOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SpurtingWaterParticle extends RisingParticle {
    private static final float STARTING_Y_SPEED = 30f;

    private final int delay;

    protected SpurtingWaterParticle(
            ClientLevel clientWorld,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            TextureAtlasSprite sprite,
            int delay
    ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ, sprite);
        this.gravity = 0.75f;
        this.lifetime += delay;
        this.delay = delay;
        this.setColor(0.2f, 0.3f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == delay) {
            this.yd = STARTING_Y_SPEED;
        }
    }

    @Override
    protected Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SpurtingWaterOption> {

        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        @Nullable
        public Particle createParticle(
                SpurtingWaterOption parameters,
                ClientLevel world,
                double x, double y, double z,
                double velocityX, double velocityY, double velocityZ,
                RandomSource random
        ) {
            return new SpurtingWaterParticle(
                    world,
                    x, y, z,
                    velocityX, velocityY, velocityZ,
                    this.spriteProvider.first(),
                    parameters.getDelay()
            );
        }
    }
}
