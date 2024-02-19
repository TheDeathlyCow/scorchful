package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.github.thedeathlycow.scorchful.util.SMth;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Optional;

public class SandstormEffects {

    public static final Vec3d REGULAR_SANDSTORM_FOG_COLOR = Vec3d.unpackRgb(0xD9AA84);
    public static final Vector3f REGULAR_SANDSTORM_PARTICLE_COLOR = Vec3d.unpackRgb(0xD9AA84).toVector3f();

    private static final float START_FOG_SPHERE_RAIN_GRADIENT = 0.75f;

    private static final float FOG_START = 4f;

    private static final float FOG_END = 16f;

    public static void onClientWorldTick(ClientWorld clientWorld) {
        if (!clientWorld.isRaining()) {
            return;
        }

        final ClientConfig config = Scorchful.getConfig().clientConfig;
        final int renderDistance = config.getSandStormParticleRenderDistance();
        if (!config.isSandstormParticlesEnabled() || renderDistance <= 0) {
            return; // config disabled
        }

        final MinecraftClient gameClient = MinecraftClient.getInstance();
        final ClientPlayerEntity mainPlayer = gameClient.player;
        final Camera camera = gameClient.gameRenderer.getCamera();
        if (mainPlayer == null || camera == null) {
            return; // no player or camera for whatever reason
        }


        // main particle loop

        final BlockPos cameraPos = camera.getBlockPos();
        final BlockPos.Mutable pos = new BlockPos.Mutable();
        final ParticleEffect particle = new DustParticleEffect(REGULAR_SANDSTORM_PARTICLE_COLOR, config.getSandStormParticleScale());
        final int rarity = config.getSandStormParticleRarity();
        final int cameraY = cameraPos.getY();
        final float particleVelocity = config.getSandStormParticleVelocity();

        for (int x = cameraPos.getX() - renderDistance; x < cameraPos.getX() + renderDistance; x++) {
            for (int z = cameraPos.getZ() - renderDistance; z < cameraPos.getZ() + renderDistance; z++) {
                int y = cameraY + clientWorld.random.nextBetween(-renderDistance / 2, (renderDistance + 1) / 2);
                y = Math.max(y, clientWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z));
                pos.set(x, y, z);
                addParticle(clientWorld, particle, pos, rarity, particleVelocity);
            }
        }
    }

    public static Optional<Vec3d> getFogColor(
            ClientWorld world, BlockPos cameraPos,
            float baseRed, float baseGreen, float baseBlue,
            float tickDelta
    ) {
        if (Sandstorms.isSandStorming(world, cameraPos)) {
            var color = new Vec3d(baseRed, baseGreen, baseBlue);
            float gradient = world.getRainGradient(1f);

            color = SMth.lerp(gradient, color, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR);

            float skyAngle = MathHelper.clamp(
                    MathHelper.cos(world.getSkyAngle(tickDelta) * 2 * MathHelper.PI) * 2.0F + 0.5F,
                    0.0F, 1.0F
            );

            color = world.getDimensionEffects().adjustFogColor(color, skyAngle);

            return Optional.of(color);
        }
        return Optional.empty();
    }

    public static void updateFogDistance(
            Camera camera,
            float viewDistance,
            CameraSubmersionType cameraSubmersionType,
            BackgroundRenderer.FogData fogData
    ) {
        Entity focused = camera.getFocusedEntity();
        World world = focused.getWorld();
        final float rainGradient = world.getRainGradient(1f);
        if (cameraSubmersionType == CameraSubmersionType.NONE && rainGradient > 0f) {
            BlockPos pos = camera.getBlockPos();
            if (Sandstorms.hasSandStorms(world.getBiome(pos))) {

                var samplePos = new BlockPos.Mutable();
                final var baseRadius = new Vec3d(fogData.fogStart, fogData.fogEnd, 0);
                final var fogRadius = new Vec3d(FOG_START, FOG_END, 0);

                // tri lerp fog distances to make less jarring biome transition
                // start is stored in X and end in Y
                Vec3d fogDistances = CubicSampler.sampleColor(camera.getPos(), (x, y, z) -> {
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

    private static void updateFogRadius(BackgroundRenderer.FogData fogData, Vec3d fogDistances, float rainGradient) {
        fogData.fogStart = MathHelper.lerp(rainGradient, fogData.fogStart, (float) fogDistances.x);
        fogData.fogEnd = MathHelper.lerp(rainGradient, fogData.fogEnd, (float) fogDistances.y);

        if (rainGradient > START_FOG_SPHERE_RAIN_GRADIENT) {
            fogData.fogShape = FogShape.SPHERE;
        }
    }

    private static void addParticle(ClientWorld world, ParticleEffect particle, BlockPos pos, int rarity, float velocity) {
        if (Sandstorms.isSandStorming(world, pos) && world.random.nextInt(rarity) == 0) {
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
