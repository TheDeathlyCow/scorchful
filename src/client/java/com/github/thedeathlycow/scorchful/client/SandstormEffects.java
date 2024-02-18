package com.github.thedeathlycow.scorchful.client;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class SandstormEffects {

    public static final Vector3f REGULAR_SANDSTORM_FOG_COLOR = Vec3d.unpackRgb(0xD9AA84).toVector3f();

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
        final ParticleEffect particle = new DustParticleEffect(REGULAR_SANDSTORM_FOG_COLOR, config.getSandStormParticleScale());
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

    public static Optional<Vector3f> getFogColor(
            World world, BlockPos cameraPos,
            float baseRed, float baseGreen, float baseBlue
    ) {
        if (Sandstorms.isSandStorming(world, cameraPos)) {
            var color = new Vector3f(baseRed, baseGreen, baseBlue);
            float gradient = world.getRainGradient(1f);
            color.x = MathHelper.lerp(gradient, color.x, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.x);
            color.y = MathHelper.lerp(gradient, color.y, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.y);
            color.z = MathHelper.lerp(gradient, color.z, SandstormEffects.REGULAR_SANDSTORM_FOG_COLOR.z);
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
        if (cameraSubmersionType == CameraSubmersionType.NONE) {
            Entity focused = camera.getFocusedEntity();
            World world = focused.getWorld();
            BlockPos pos = camera.getBlockPos();
            if (Sandstorms.isSandStorming(world, pos)) {
                fogData.fogStart = 4f;
                fogData.fogEnd = 16f;
                fogData.fogShape = FogShape.SPHERE;
            }
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
