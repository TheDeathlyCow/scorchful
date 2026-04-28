package com.github.thedeathlycow.scorchful.client.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.particle.DustGrainParticleEffect;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.github.thedeathlycow.scorchful.util.SMth;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;

public class SandstormEffects {

    public static final Vec3 REGULAR_SANDSTORM_FOG_COLOR = Vec3.fromRGB24(0xD9AA84);
    public static final Vector3f REGULAR_SANDSTORM_PARTICLE_COLOR = Vec3.fromRGB24(0xD9AA84).toVector3f();

    private static final float START_FOG_SPHERE_RAIN_GRADIENT = 0.75f;

    private static final float PARTICLE_SCALE = 10f;

    public static boolean shouldCancelClouds(ClientLevel world, BlockPos pos) {
        return world.getRainLevel(1f) > SandstormEffects.START_FOG_SPHERE_RAIN_GRADIENT
                && Sandstorms.isSandStorming(world, pos);
    }

    public static void tickSandstormParticles(ClientLevel clientWorld) {
        if (!clientWorld.isRaining() || clientWorld.tickRateManager().isFrozen()) {
            return;
        }

        final ClientConfig config = Scorchful.getConfig().clientConfig;
        final int renderDistance = config.getSandStormParticleRenderDistance();
        if (!config.isSandstormParticlesEnabled() || renderDistance <= 0) {
            return; // config disabled
        }

        final Minecraft gameClient = Minecraft.getInstance();
        final Camera camera = gameClient.gameRenderer.getMainCamera();
        if (camera == null) {
            return; // no camera for whatever reason
        }

        // main particle loop
        final BlockPos cameraPos = camera.getBlockPosition();
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        final ParticleOptions particle = new DustGrainParticleEffect(REGULAR_SANDSTORM_PARTICLE_COLOR, PARTICLE_SCALE);
        final int rarity = config.getSandStormParticleRarity();
        final int cameraY = cameraPos.getY();
        final float particleVelocity = config.getSandStormParticleVelocity();

        for (int x = cameraPos.getX() - renderDistance; x < cameraPos.getX() + renderDistance; x++) {
            for (int z = cameraPos.getZ() - renderDistance; z < cameraPos.getZ() + renderDistance; z++) {
                int y = cameraY + clientWorld.random.nextIntBetweenInclusive(-renderDistance / 2, (renderDistance + 1) / 2);
                y = Math.max(y, clientWorld.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z));
                pos.set(x, y, z);
                addParticle(clientWorld, particle, pos, rarity, particleVelocity);
            }
        }
    }

    public static Optional<Vec3> getFogColor(
            ClientLevel world, Camera camera,
            float baseRed, float baseGreen, float baseBlue,
            float tickDelta
    ) {
        ClientConfig config = Scorchful.getConfig().clientConfig;

        if (!config.isSandstormFogEnabled()) {
            return Optional.empty();
        }

        float gradient = world.getRainLevel(1f);
        if (gradient > 0f && Sandstorms.isSandStorming(world, camera.getBlockPosition())) {
            final var normalColor = new Vec3(baseRed, baseGreen, baseBlue);
            Vec3 adjustedColor = SMth.lerp(gradient, normalColor, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR);

            // idk why the game does this transformation but ill do it here too for consistency
            float skyAngle = Mth.clamp(
                    Mth.cos(world.getTimeOfDay(tickDelta) * 2 * Mth.PI) * 2.0F + 0.5F,
                    0.0F, 1.0F
            );

            final Vec3 sandStormColor = adjustedColor;
            var samplePos = new BlockPos.MutableBlockPos();
            adjustedColor = CubicSampler.gaussianSampleVec3(
                    camera.getPosition(),
                    (x, y, z) -> {
                        samplePos.set(x, y, z);
                        Holder<Biome> biome = world.getBiome(samplePos);
                        return world.effects().getBrightnessDependentFogColor(
                                Sandstorms.hasSandStorms(biome)
                                        ? sandStormColor
                                        : normalColor,
                                skyAngle
                        );
                    }
            );

            return Optional.of(adjustedColor);
        }
        return Optional.empty();
    }

    public static void updateFogDistance(
            Camera camera,
            float viewDistance,
            FogType cameraSubmersionType,
            FogRenderer.FogData fogData
    ) {
        ClientConfig config = Scorchful.getConfig().clientConfig;

        if (!config.isSandstormFogEnabled()) {
            return;
        }

        Entity focused = camera.getEntity();
        Level world = focused.level();
        final float rainGradient = world.getRainLevel(1f);
        if (cameraSubmersionType == FogType.NONE && rainGradient > 0f) {
            BlockPos pos = camera.getBlockPosition();
            if (Sandstorms.isSandStorming(world, pos)) {
                var samplePos = new BlockPos.MutableBlockPos();
                final var baseRadius = new Vec3(fogData.start, fogData.end, 0);
                final var fogRadius = new Vec3(
                        config.getSandStormFogStart(),
                        config.getSandStormFogEnd(),
                        0
                );

                // tri lerp fog distances to make less jarring biome transition
                // start is stored in X and end in Y
                Vec3 fogDistances = CubicSampler.gaussianSampleVec3(camera.getPosition(), (x, y, z) -> {
                    samplePos.set(x, y, z);
                    if (Sandstorms.hasSandStorms(world.getBiome(samplePos))) {
                        return fogRadius;
                    }
                    return baseRadius;
                });

                // lerp fog distances for smooth transition when weather changes
                updateFogRadius(fogData, fogDistances, rainGradient);
            }
        }
    }

    private static void updateFogRadius(FogRenderer.FogData fogData, Vec3 fogDistances, float rainGradient) {
        fogData.start = Mth.lerp(rainGradient, fogData.start, (float) fogDistances.x);
        fogData.end = Mth.lerp(rainGradient, fogData.end, (float) fogDistances.y);

        if (rainGradient > START_FOG_SPHERE_RAIN_GRADIENT) {
            fogData.shape = FogShape.SPHERE;
        }
    }

    private static void addParticle(ClientLevel world, ParticleOptions particle, BlockPos pos, int rarity, float velocity) {
        if (Sandstorms.getCurrentSandStorm(world, pos) != Sandstorms.SandstormType.NONE && world.random.nextInt(rarity) == 0) {
            world.addParticle(
                    particle,
                    pos.getX() + world.random.nextDouble(),
                    pos.getY() + world.random.nextDouble(),
                    pos.getZ() + world.random.nextDouble(),
                    velocity, 0, 0
            );
        }
    }

    private SandstormEffects() {

    }
}
